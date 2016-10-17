package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;


public class OHCarrollCountyParser extends DispatchProQAParser {
  
  private static final Pattern IAM_MASTER = Pattern.compile("JCOM\\.\\.\\. RC:Emergency/(.*)/(Run# \\d+)");
  private static final Pattern DIR_OF_PTN = Pattern.compile("\\b([NSEW])/(O)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern DIR_OF_PTN2 = Pattern.compile("\\b([NSEW])-(O)\\b", Pattern.CASE_INSENSITIVE);

  public OHCarrollCountyParser() {
    super(CITY_LIST, "CARROLL COUNTY", "OH",
           "CALL ADDR ( PLACE CITY | CITY | ) INFO+? TIME! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "iamresponding.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Try to back out what we presume to be IAM scrambling of a standard ProQA page format
    Matcher match = IAM_MASTER.matcher(body);
    if (match.matches()) {
      body = "RC:" + match.group(2) + '/' + match.group(1);
    }
    
    // N/O type constructs confuse the parse and need to be escaped
    body = DIR_OF_PTN.matcher(body).replaceAll("$1-$2");
    return super.parseMsg(body, data);
  }
  
  private class MyInfoField extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<Unknown>")) return;
      
      field = DIR_OF_PTN2.matcher(field).replaceAll("$1/$2");
      String chkField = DIR_OF_PTN.matcher(field).replaceAll("").trim();
      if (isValidAddress(chkField)) {
        data.strCross = append(data.strCross, " / ", field);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X " + super.getFieldNames();
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return DIR_OF_PTN.matcher(addr).replaceAll("").trim();
  }
  
  private static final String[] CITY_LIST = new String[] {
    // Villages
    "CARROLLTON",
    "DELLROY",
    "LEESVILLE",
    "MAGNOLIA",
    "MALVERN",
    "MINERVA",
    "SHERRODSVILLE",

    // Townships
    "AUGUSTA TWP",
    "BROWN TWP",
    "CENTER TWP",
    "EAST TWP",
    "FOX TWP",
    "HARRISON TWP",
    "LEE TWP",
    "LOUDON TWP",
    "MONROE TWP",
    "ORANGE TWP",
    "PERRY TWP",
    "ROSE TWP",
    "UNION TWP",
    "WASHINGTON TWP",
    
    // Unincorporated communities
    "AUGUSTA",
    "HARLEM SPRINGS",
    "KILGORE",
    "LEAVITTSVILLE",
    "LINDENTREE",
    "MECHANICSTOWN",
    "MORGES",
    "NEW HARRISBURG",
    "PATTERSONVILLE",
    "PETERSBURG",
    "SCROGGSFIELD",
    "WATTSVILLE",
    
    // Harrison County
    "BOWERSTON",
    
    // Columbiana County
    "SALINEVILLE"
  };
}
