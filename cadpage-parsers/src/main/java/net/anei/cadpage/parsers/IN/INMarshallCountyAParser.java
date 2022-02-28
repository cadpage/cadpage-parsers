package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class INMarshallCountyAParser extends DispatchOSSIParser {

  public INMarshallCountyAParser() {
    super(CITY_CODES, "MARSHALL COUNTY", "IN",
           "( CANCEL ( CITY/Y ADDR | ADDR CITY/Y! PLACE ) INFO+ | FYI ( PLACE ADDR/Z CITY/Y | ADDR/Z CITY/Y | DPLACE? ADDR ) X/Z+? CALL! END ) DATETIME");
    setupCities(CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "cad@co.marshall.in.us,noreply@co.marshall.in.us,alerts@etieline.com,5742617686,5742081200";
  }

  private static final Pattern BAD_CALL_PTN = Pattern.compile("[A-Z]{1,4}|952");

  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("Update: ;") || body.startsWith("FYI: ;")) {
      body = "CAD:" + body;
    }
    if (!super.parseMsg(body, data)) return false;

    // INKosciuskoCounty calls should be rejected.  The pass everything else
    // but end up with their city code in the call field
    if (BAD_CALL_PTN.matcher(data.strCall).matches()) return false;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DPLACE")) return new SkipField("Some Location");
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Some Location")) return;
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("(S) (N)")) {
        data.strPlace = append(data.strPlace, " - ", field.substring(7).trim());
      } else {
        super.parse(field, data);
      }
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = MISSING_SPACE_PTN.matcher(addr);
    if (match.matches()) addr = match.group(1) + ' ' + match.group(2);
    return super.adjustMapAddress(addr);
  }
  private static final Pattern MISSING_SPACE_PTN = Pattern.compile("(\\d+)(?:-\\d+)?(?!TH\\b|B )([A-Z].*)");

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARGO", "ARGOS",
      "BOUR", "BOURBON",
      "BREM", "BREMEN",
      "CULV", "CULVER",
      "DONA", "DONALDSON",
      "LAPA", "LAPAZ",
      "PLYM", "PLYMOUTH",
      "TIPP", "TIPPECANOE",
      "TYNE", "TYNER",

      "ETNA", "ETNA GREEN",
      "GROV", "GROVERTOWN",
      "KNOX", "KNOX",
      "LAKE", "LAKEVILLE",
      "MENT", "MENTONE",
      "NAPP", "NAPPANEE",
      "ROCH", "ROCHESTER",
      "WALK", "WALKERTON"
  });

  private static final String[] CITY_LIST = new String[]{
    "ELKHART COUNTY",
    "ELKHART",
    "ST JOSEPH COUNTY",
    "ST JOSEPH"
  };
}
