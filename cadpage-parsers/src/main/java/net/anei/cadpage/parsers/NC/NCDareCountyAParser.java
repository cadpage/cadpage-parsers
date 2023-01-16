package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCDareCountyAParser extends DispatchOSSIParser {

  public NCDareCountyAParser() {
    super("DARE COUNTY", "NC", "SRC? CALL ADDR SRC? ( PLACE CITY | CITY | ) INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@darenc.com,CAD@darepublicsafety.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("SRC")) return new SourceField("(?:ST|MP)\\d{2}(?: \\(NC\\d{2}\\))?", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }



  private static Pattern CALL_CODE_PTN = Pattern.compile("(.*?) *\\((\\d{2}-[A-Z]-\\d[A-Z]?)\\)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = CALL_CODE_PTN.matcher(field);
      if (mat.matches()) {
        field = mat.group(1);
        data.strCode = mat.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private class MyCityField extends Field {
    public MyCityField() {
      super("[A-Z]{3}", true);
    }

    @Override
    public void parse(String field, Data data) {
      if(CITY_CODES.containsKey(field)) data.strCity = CITY_CODES.getProperty(field);
      else abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) {
        String city = NCDareCountyBParser.CITY_CODES.getProperty(field);
        if (city != null) {
          data.strCity = city;
          return;
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO CITY";
    }
  }

  private static final Properties CITY_CODES = NCDareCountyBParser.CITY_CODES;
}
