package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXDallasCountyCParser extends DispatchOSSIParser {

  public TXDallasCountyCParser() {
    super(CITY_CODES, "DALLAS COUNTY", "TX",
          "( CANCEL ADDR CITY? " +
          "| UNIT/Z ENROUTE ADDR CITY CALL/SDS END " +
          "| FYI? ID MAP UNIT CALL ADDR X X CITY PLACE CH! "+
          ") INFO/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }


  private static final Pattern STREET_APT_ADDR_PTN = Pattern.compile("(\\d+)-(\\d+) +(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;

//    body = body.replace("\n", "");

    if (body.contains(",Enroute,")) {
      if (!super.parseFields(body.split(","), data)) return false;
    }
    else {
      if (!super.parseMsg(body, data)) return false;
    }

    // A leading nnn-nnn address might be a regular address range
    // but more often that not it will be address-apt combination.
    // See if we can figure them out
    Matcher match = STREET_APT_ADDR_PTN.matcher(data.strAddress);
    if (match.matches()) {
      String stno1 = match.group(1);
      String stno2 = match.group(2);
      if (stno1.length() != stno2.length() ||
          stno1.length() < 3 ||
          !stno1.substring(0,2).equals(stno2.substring(0,2))) {
        data.strApt = append(stno2, "-", data.strApt);
        data.strAddress = stno1 + ' ' + match.group(3);
      }
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ENROUTE")) return new CallField("Enroute", true);
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("MAP")) return new MapField("\\d{4}|", true);
    if (name.equals("CH")) return new ChannelField("TAC *\\d+|", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern TAC_PTN = Pattern.compile("TAC *\\S*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (TAC_PTN.matcher(field).matches()) {
        data.strChannel = field;
      } else if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(14).trim();
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "CH? INFO";
    }
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("DFW AIRPORT")) city = "DALLAS/FORT WORTH AIRPORT";
    return super.adjustMapCity(city);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ADDI", "ADDISON",
      "CARR", "CARROLLTON",
      "COPP", "COPPELL",
      "DALL", "DALLAS",
      "DECO", "DENTON COUNTY",
      "DFW",  "DFW AIRPORT",
      "FARM", "FARMERS BRANCH",
      "FLOW", "FLOWER MOUND",
      "FRIS", "FRISCO",
      "GRAP", "GRAPEVINE",
      "HEBR", "HEBRON",
      "IRVI", "IRVING",
      "LEWI", "LEWISVILLE",
      "PLAN", "PLANO",
      "PRI",  "PRINCETON",
      "THEC", "THE COLONY"
  });

}
