package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MOStLouisCountyGParser extends DispatchGlobalDispatchParser {
  
  
  public MOStLouisCountyGParser() {
    super(CITY_TABLE, "ST LOUIS COUNTY", "MO", LEAD_SRC_UNIT_ADDR,
          Pattern.compile("FD|STILL"), Pattern.compile("[A-Z]{0,2}\\d{4}"));
    setupCallList(CALL_LIST);

  }
  
  @Override
  public String getFilter() {
    return "Kirkwood@Kirkwoodmo.org";
  }
  
  private static final Pattern TAC_PTN = Pattern.compile(" *\\b((?:DUTY )?TAC \\S+)\\b *");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = TAC_PTN.matcher(body);
    if (match.find()) {
      data.strChannel = match.group(1);
      body = append(body.substring(0,match.start()), " ", body.substring(match.end()));
    }
    if (!super.parseMsg(body, data)) return false;
    if (data.strApt.equals("-")) data.strApt = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "CH " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  protected class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, " MO");
      super.parse(field, data);
    }
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ASSIST PATIENT",
      "BASIC EMS",
      "CAR FIRE WITH EXPOSURES",
      "CHECK THE AREA",
      "ELEVATOR RESCUE",
      "EXTERIOR GAS LEAK",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENTIAL",
      "FIRE INSPECTION",
      "GAS LEAK",
      "LIFE THREAT EMS",
      "LOCK OUT",
      "MUTUAL AID",
      "MVA WITH INJURIES",
      "RESIDENTIAL LOCK OUT",
      "SMELL OF SMOKE COMMERCIAL",
      "TRANSFORMER FIRE",
      "VEHICLE LOCKOUT",
      "WIRES DOWN"

  );

  private static final String[] CITY_TABLE = new String[]{
      "BALLWIN",
      "BELLA VILLA",
      "BELLEFONTAINE NEIGHBORS",
      "BELLERIVE",
      "BEL-NOR",
      "BEL-RIDGE",
      "BERKELEY",
      "BEVERLY HILLS",
      "BLACK JACK",
      "BRECKENRIDGE HILLS",
      "BRENTWOOD",
      "BRIDGETON",
      "CALVERTON PARK",
      "CHAMP",
      "CHARLACK",
      "CHESTERFIELD",
      "CLARKSON VALLEY",
      "CLAYTON",
      "COOL VALLEY",
      "COUNTRY CLUB HILLS",
      "COUNTRY LIFE ACRES",
      "CRESTWOOD",
      "CREVE COEUR",
      "CRYSTAL LAKE PARK",
      "DELLWOOD",
      "DES PERES",
      "EDMUNDSON",
      "ELLISVILLE",
      "EUREKA",
      "FENTON",
      "FERGUSON",
      "FLORDELL HILLS",
      "FLORISSANT",
      "FRONTENAC",
      "GLENCOE",
      "GLENDALE",
      "GLEN ECHO PARK",
      "GRANTWOOD VILLAGE",
      "GREENDALE",
      "GREEN PARK",
      "HANLEY HILLS",
      "HAZELWOOD",
      "HILLSDALE",
      "HUNTLEIGH",
      "JENNINGS",
      "KINLOCH",
      "KIRKWOOD",
      "LADUE",
      "LAKESHIRE",
      "MACKENZIE",
      "MANCHESTER",
      "MAPLEWOOD",
      "MARLBOROUGH",
      "MARYLAND HEIGHTS",
      "MOLINE ACRES",
      "NORMANDY",
      "NORTHWOODS",
      "NORWOOD COURT",
      "OAKLAND",
      "OLIVETTE",
      "OVERLAND",
      "PACIFIC",
      "PAGEDALE",
      "PASADENA HILLS",
      "PASADENA PARK",
      "PINE LAWN",
      "RICHMOND HEIGHTS",
      "RIVERVIEW",
      "ROCK HILL",
      "SHREWSBURY",
      "ST ANN",
      "ST JOHN",
      "SUNSET HILLS",
      "SYCAMORE HILLS",
      "TOWN AND COUNTRY",
      "TWIN OAKS",
      "UNIVERSITY CITY",
      "UPLANDS PARK",
      "VALLEY PARK",
      "VELDA CITY",
      "VELDA VILLAGE HILLS",
      "VINITA PARK",
      "VINITA TERRACE",
      "WARSON WOODS",
      "WEBSTER GROVES",
      "WELLSTON",
      "WESTWOOD",
      "WILBUR PARK",
      "WILDWOOD",
      "WINCHESTER",
      "WOODSON TERRACE"
  };
}
