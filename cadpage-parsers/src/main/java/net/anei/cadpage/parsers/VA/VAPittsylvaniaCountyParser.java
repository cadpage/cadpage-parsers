package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class VAPittsylvaniaCountyParser extends DispatchOSSIParser {
 
  public VAPittsylvaniaCountyParser() {
    super(CITY_CODES, "PITTSYLVANIA COUNTY", "VA",
          "ID?: EMPTY? ( CANCEL ADDR! CITY?| FYI? CALL ADDR! ( CITY! ID? | ID! ) X? X? ) INFO+");
    setupProtectedNames("OAKS AND STANFIELD", "R AND L SMITH");
  }
  
  @Override
  public String getFilter() {
    return "CAD@pittgov.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_DIRO;
  }
  
  private static final Pattern LEADER = Pattern.compile("^\\d+:(?!CAD:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Text Message / ");
    Matcher match = LEADER.matcher(body);
    if (match.find()) {
      body = body.substring(0,match.end()) + "CAD:" + body.substring(match.end());
    } else if (!body.startsWith("CAD:")) {
      body = "CAD:" + body;
          
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private Pattern US_HWY_NN_PTN = Pattern.compile("U ?S HIGHWAY NO \\d+");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (US_HWY_NN_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return true;
      } else {
        return super.checkParse(field, data);
      }
    }
  }
 
  
  @Override
  public String adjustMapAddress(String addr) {
    return US_PTN.matcher(addr).replaceAll("US");
  }
  private static final Pattern US_PTN = Pattern.compile("\\bU S\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES= buildCodeTable(new String[]{
      "BLA","Blairs",
      "CAL","Callands",
      "CAS","Cascade",
      "CHA","Chatham",
      "DAN","Danville",
      "DRY","Dry Fork",
      "GRE","Gretna",
      "HUR","Hurt",
      "JAV","Java",
      "KEE","Keeling",
      "LON","Long Island",
      "PIT","Pittsville",
      "RIN","Ringgold",
      "RNC","RNC",
      "RNG","Ringgold",
      "SAN","Sandy Level",
      "SUT","Sutherlin",
      
      "CAM","Campbell County",
      
      // Henry County
      "HEN","Henry County",
      "AXT","Axton",
      
      "FRA","Franklin County",
      "PEN","Penhook",

      "HAL","Halifax County"

  });
}
