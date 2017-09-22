/**
 * Instantiates a data source object based on information in a
 * specification file (JSON format).
 */

package core.spec;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import core.data.DataSource;
import core.data.Param;
import core.log.Errors;
import core.schema.CompField;
import core.schema.CompSchema;
import core.schema.ISchema;
import core.schema.ListSchema;
import core.schema.PrimSchema;
import core.util.FileLoader;

public class DataSpecLoader {

    private DataSource ds;

    public DataSpecLoader(String specpath) {
        this(new FileLoader().createInput(specpath));
    }

    @SuppressWarnings("unchecked")
    public DataSpecLoader(InputStream is) {
        JSONTokener jt = new JSONTokener(is);
        Object obj = jt.nextValue();
        if (!isValidDataSourceSpec(obj)) {
            throw Errors.exception(DataSpecException.class, "ds:invalid-spec");
        }

        try {
            Map<String, Object> m = ((JSONObject) obj).toMap();

            if (m.containsKey("format")) {
                ds = DataSource.connectAs((String)m.get("format"), (String)m.get("path"));
            } else {
                ds = DataSource.connect((String)m.get("path"));
            }

            // TODO: move all the following into an import() method in DataSource (?)
            ds.setName((String)m.getOrDefault("name", null));
            ds.setInfoURL((String)m.getOrDefault("infourl", null));
            ds.setDescription((String)m.getOrDefault("description", null));

            if (m.containsKey("options")) {
                List<Map<String,Object>> opts = (List<Map<String, Object>>) m.get("options");
                for (Map<String,Object> opt : opts) {
                    ds.setOption(opt.get("name").toString(), opt.get("value").toString());
                }
            }
            
            if (m.containsKey("params")) {
                List<Map<String,Object>> params = (List<Map<String, Object>>) m.get("params");
                for (Map<String,Object> pm : params) {
                    Param param = new Param(pm);
                    ds.addParam(param);
                    if (pm.containsKey("value")) {
                        ds.setParam(pm.get("key").toString(), pm.get("value").toString());
                    }
                }
            }
            
            if (m.containsKey("cache")) {
                Map<String,Object> cacheOpts = (Map<String,Object>)m.get("cache");
                if (cacheOpts.containsKey("timeout"))
                    ds.setCacheTimeout(Integer.parseInt(cacheOpts.get("timeout").toString()));
                if (cacheOpts.containsKey("directory"))
                    ds.setCacheDirectory(cacheOpts.get("directory").toString());
            }
            
            if (m.containsKey("schema")) {
                ds.setSchema(constructSchema((Map<String,Object>)m.get("schema")));
            }

        } catch (ClassCastException e) {
            throw Errors.exception(DataSpecException.class, "ds:invalid-spec");
        }

    }

    @SuppressWarnings("unchecked")
    private ISchema constructSchema(Map<String, Object> map) {
        ISchema sch = null;
        String type = (String) map.get("type");
        
        if (type.equals("prim")) {
            sch = new PrimSchema((String) map.getOrDefault("path", null),
                     (String) map.getOrDefault("description", null));
        } else if (type.equals("list")) {
            ISchema eltSch = constructSchema((Map<String, Object>) map.get("elements"));
            sch = new ListSchema((String) map.getOrDefault("path", null),
                     (String) map.getOrDefault("description", null),
                     eltSch);
        } else if (type.equals("struct")) {
            List<Map<String,Object>> fields = (List<Map<String, Object>>) map.get("fields");
            ArrayList<CompField> cfs = new ArrayList<CompField>();
            
            for (Map<String,Object> field : fields) {
                CompField cf = new CompField((String) field.get("name"), 
                                            constructSchema((Map<String, Object>) field.get("schema")));
                cfs.add(cf);
            }
            
            sch = new CompSchema((String) map.getOrDefault("path", null),
                    (String) map.getOrDefault("description", null),
                    cfs.toArray(new CompField[] {}));
        }
        
        return sch;
    }

    private boolean isValidDataSourceSpec(Object obj) {
        if (!(obj instanceof JSONObject)) 
            return false;

        JSONObject jobj = (JSONObject) obj;

        return jobj.has("path");
    }

    public DataSource getDataSource() {
        return this.ds;
    }

}
