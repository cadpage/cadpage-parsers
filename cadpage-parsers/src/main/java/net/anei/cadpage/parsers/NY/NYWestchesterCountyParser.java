package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;



public class NYWestchesterCountyParser extends FieldProgramParser {
  
  public NYWestchesterCountyParser() {
    super(CITY_CODES, "WESTCHESTER COUNTY", "NY",
           "ADDR Cross:X! Type:CALL! CALL Time_out:TIME Area:CITY lev:PRI Comments:INFO% INFO+");
  }
  
  @Override
  public String getFilter() {
    return "IPAGE@westchestergov.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Check for IPage signature
    do {
      if (subject.equals("IPage")) break;
      if (subject.equals("Email Copy Message From Hiplink")) break;
      if (body.startsWith("IPage / ")) {
        body = body.substring(8).trim();
        break;
      }
      if (body.startsWith("- ")) {
        body = body.substring(2).trim();
        break;
      }
      if (isPositiveId()) break;
      return false;
    } while (false);
    
    body = body.replace(" Area:", ",Area:");
    if (!parseFields(body.split(","), data)) return false;
    int pt = data.strCity.indexOf(',');
    if (pt >= 0) {
      data.strState = data.strCity.substring(pt+1).trim();
      data.strCity = data.strCity.substring(0,pt).trim();
    }
    if (data.strCity.equals("BANKSVILLE")) {
      data.strPlace = append(data.strPlace, " - ", data.strCity);
      data.strCity = "BEDFORD";
    }
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strApt = p.getLastOptional(":APT ");
      data.strPlace = p.getLastOptional(": @");
      if (data.strPlace.length() == 0) data.strPlace = p.getLastOptional(':');
      data.strCity = p.getLast(' ');
      parseAddress(p.get(), data);
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
      if (data.strCity.endsWith("_T")) {
        data.strCity = data.strCity.substring(0,data.strCity.length()-2);
      }
      data.strCity = data.strCity.replace('_', ' ');
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST PLACE APT";
    }
  }
  
  private static final Pattern GPS_PTN1 = Pattern.compile("WPH\\d +([-+]?\\d{3}\\.\\d{6,}[, ][-+]?\\d{3}\\.\\d{6,})");
  private static final Pattern GPS_PTN2 = Pattern.compile("http://maps.google.com/\\?q=([-+]?\\d{2,3}\\.\\d{6,})");
  private static final Pattern GPS_PTN3 = Pattern.compile("([-+]?\\d{2,3}\\.\\d{6,})");
  private class MyInfoField extends InfoField {
    
    private String gps1 = null;
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN1.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1), data);
        return;
      }
      
      match = GPS_PTN2.matcher(field);
      if (match.matches()) {
        gps1 = match.group(1);
        return;
      }
      if (gps1 != null) {
        match = GPS_PTN3.matcher(field);
        if (data.strGPSLoc.length() == 0 && match.matches()) {
          setGPSLoc(gps1+',' +  match.group(1), data);
        }
        gps1 = null;
        return;
      }
      
      data.strSupp = append(data.strSupp, ", ", field);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AIRPT",      "WESTCHESTER AIRPRT",
      "ARCHV",      "ARCHVILLE",
      "ARDSL",      "ARDSLEY",
      "ARMNK",      "ARMONK",
      "BANKS",      "BANKSVILLE",
      "BEDHL",      "BEDFORD HILLS",
      "BEDVL",      "BEDFORD",
      "BCLFM",      "BRIARCLIFF MANOR",
      "CHAPQ",      "CHAPPAQUA",
      "CROFL",      "CROTON FALLS",
      "CROTN",      "CROTON",
      "DBSFY",      "DOBBS FERRY",
      "ECHST",      "EASTCHESTER",
      "ELMSF",      "ELMSFORD",
      "FAIRV",      "FAIRVIEW",
      "CNTVL",      "CONTINENTAL",
      "GBRDG",      "GOLDENS BRIDGE",
      "GNWCH",      "GREENWICH,CT",
      "GRASS",      "GRASSLANDS",
      "GRNVL",      "GREENVILLE",
      "HARSN",      "HARRISON",
      "HARTS",      "HARTSDALE",
      "HASTG",      "HASTINGS",
      "HAWTH",      "HAWTHORNE",
      "IRVNG",      "IRVINGTON",
      "KATNH",      "KATONAH",
      "LARCH",      "LARCHMONT",
      "MAMTW",      "MAMARONECK",
      "MAMVL",      "MAMARONECK",
      "MILLW",      "MILLWOOD",
      "MOHGN",      "MOHEGAN",
      "MONTR",      "MONTROSE",
      "MTKSC",      "MOUNT KISCO",
      "MTVRN",      "MOUNT VERNON",
      "NROCH",      "NEW ROCHELLE",
      "SLPHL",      "SLEEPY HOLLOW",
      "NOWPL",      "NORTH WHITE PLAINS",
      "OSSNG",      "OSSINING",
      "PKSKL",      "PEEKSKILL",
      "PELHM",      "PELHAM",
      "PELMR",      "PELHAM MANOR",
      "POCHL",      "POCANTICO HILLS",
      "PORTC",      "PORT CHESTER",
      "PNDRG",      "POUND RIDGE",
      "PVILL",      "PLEASANTVILLE",
      "RYBRK",      "RYE BROOK",
      "RYE",        "RYE",
      "SCARS",      "SCARSDALE",
      "SOMER",      "SOMERS",
      "SSALM",      "SOUTH SALEM",
      "TARRY",      "TARRYTOWN",
      "THRWD",      "THORNWOOD",
      "VALHA",      "VALHALLA",
      "VERPL",      "VERPLANCK",
      "WHRSN",      "WEST HARRISON",
      "WHPLN",      "WHITE PLAINS",
      "YKTWN",      "YORKTOWN",
      "YNKRS",      "YONKERS",
      "BUCHN",      "BUCHANAN",
      "VISTA",      "VISTA",
      "VAHOS",      "MONTROSE"
  });
}
	