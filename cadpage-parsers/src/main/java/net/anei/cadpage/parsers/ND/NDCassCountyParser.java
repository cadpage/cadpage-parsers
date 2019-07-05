package net.anei.cadpage.parsers.ND;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NDCassCountyParser extends FieldProgramParser {
 
  public NDCassCountyParser() {
    super(CITY_CODES, "CASS COUNTY", "ND",
          "MASH! CFS_#:ID_INFO! INFO/N+ Units:UNIT! GPS:GPS! X END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@fargond.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Info")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MASH")) return new MyMashField();
    if (name.equals("ID_INFO")) return new MyIdInfoField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private static final Pattern MASH_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?) +([^*]+?) \\* ([^,]+?)(?:, *([A-Z]+))? \\*(?: (.*))?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final Pattern TRAIL_CITY_PTN = Pattern.compile("(.*) ([A-Z]{3,})(?: ([NSEW]))?");
  private static final Pattern BOUND_PTN = Pattern.compile("[NSEW]B");
  
  private class MyMashField extends Field {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = MASH_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, match.group(2), data);
      } else {
        data.strTime = time;
      }
      data.strCall = match.group(3).trim();
      String addr = match.group(4);
      parseAddress(addr.replace('\\', '&').replace('@', '&'), data);
      String city = match.group(5);
      if (city != null) data.strCity = convertCodes(city, CITY_CODES);
      String place = getOptGroup(match.group(6));
      place = place.replace("Fire - Station", "Fire Station");
      int pt = place.indexOf(" - ");
      if (pt >= 0) place = place.substring(0, pt).trim();
      place = stripFieldEnd(place, addr);
      data.strPlace = place;
      
      // Some alternative city constructs
      if (data.strCity.length() == 0) {
        if (data.strPlace.equals("HILSBORO")) {
          data.strCity = "HILLSBORO";
          data.strState = "ND";
          data.strPlace = "";
        } else {
          match = TRAIL_CITY_PTN.matcher(data.strAddress);
          if (match.matches()) {
            city = CITY_CODES.getProperty(match.group(2));
            if (city != null) {
              data.strCity = city;
              data.strAddress = match.group(1);
              String suffix = match.group(3);
              if (suffix != null) data.strAddress = data.strAddress + ' ' + suffix;
            }
          }
        }
      }
      
      // Separate city and state
      pt = data.strCity.indexOf('/');
      if (pt >= 0) {
        data.strState = data.strCity.substring(pt+1);
        data.strCity = data.strCity.substring(0, pt);
      }
      
      // If place name consists of a direction, append it to address
      if (BOUND_PTN.matcher(data.strPlace).matches()) {
        data.strAddress = append(data.strAddress, " ", data.strPlace);
        data.strPlace = "";
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CALL ADDR APT CITY ST PLACE" ;
    }
  }
  
  private static final Pattern INFO_ID_PTN = Pattern.compile("(\\d+)\\b *(.*)");  
  
  private class MyIdInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strSupp = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "ID INFO";
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ABSA", "ABSARAKA/ND",
      "ALIC", "ALICE/ND",
      "AMEN", "AMENIA/ND",
      "ARGU", "ARGUSVILLE/ND",
      "ARTH", "ARTHUR/ND",
      "AVIL", "AVERILL/MN",
      "AYR",  "AYR/ND",
      "BAKE", "BAKER/MN",
      "BARN", "BARNESVILLE/MN",
      "BORU", "BORUP/MN",
      "BRIA", "BRIARWOOD/ND",
      "BUFF", "BUFFALO/ND",
      "CASS", "CASS COUNTY/ND",
      "CAST", "CASSELTON/ND",
      "CHAF", "CHAFFEE/ND",
      "CHRI", "CHRISTINE/ND",
      "CLAY", "CLAY COUNTY/MN",
      "CLIF", "CLIFFORD/ND",
      "COLG", "COLGATE/ND",
      "COMS", "COMSTOCK/MN",
      "CROM", "CROMWELL TOWNSHIP/MN",
      "DALE", "DALE/MN",
      "DAVE", "DAVENPORT/ND",
      "DILW", "DILWORTH/MN",
      "DOWN", "DOWNER/MN",
      "DURB", "DURBIN/ND",
      "EGLO", "EGLON TOWNSHIP/MN",
      "ELKT", "ELKTON TOWNSHIP/MN",
      "ELMW", "ELMWOOD TOWNSHIP/MN",
      "EMBD", "EMBDEN/ND",
      "ENDE", "ENDERLIN/ND",
      "ERIE", "ERIE/ND",
      "FELT", "FELTON/MN",
      "FGO",  "FARGO/ND",
      "FING", "FINGAL/ND",
      "FLOW", "FLOWING TOWNSHIP/MN",
      "FRON", "FRONTIER/ND",
      "GALE", "GALESBURG/ND",
      "GARD", "GARDNER/ND",
      "GEOR", "GEORGETOWN/MN",
      "GLYN", "GLYNDON/MN",
      "GOOS", "GOOSE PRAIRIE TOWNSHIP/MN",
      "GRAN", "GRANDIN/ND",
      "HAGE", "HAGEN TOWNSHIP/MN",
      "HAWL", "HAWLEY/MN",
      "HARW", "HARWOOD/ND",
      "HICK", "HICKSON/ND",
      "HIGH", "HIGHLAND GROVE TOWNSHIP/MN",
      "HITT", "HITTERDAL/MN",
      "HOLY", "HOLY CROSS TOWNSHIP/MN",
      "HOPE", "HOPE/ND",
      "HORA", "HORACE/ND",
      "HUMB", "HUMBOLDT TOWNSHIP/MN",
      "HUNT", "HUNTER/ND",
      "KEEN", "KEENE TOWNSHIP/MN",
      "KIND", "KINDRED/ND",
      "KRAG", "KRAGNESS TOWNSHIP/MN",
      "KURT", "KURTZ TOWNSHIP/MN",
      "LAKE", "LAKE PARK/MN",
      "LEON", "LEONARD/ND",
      "LPRK", "LAKE PARK/MN",
      "MAPL", "MAPLETON/ND",
      "MHD",  "MOORHEAD/MN",
      "MOLA", "MOLAND TOWNSHIP/MN",
      "MORK", "MORKEN TOWNSHIP/MN",
      "NEWR", "NEW ROCKFORD/ND",
      "NORA", "HORACE/ND",
      "NROT", "NORTH RIVER/ND",
      "OAKP", "OAKPORT TOWNSHIP/MN",
      "ORIS", "ORISKA/ND",
      "OXBO", "OXBOW/ND",
      "PAGE", "PAGE/ND",
      "PARK", "PARKE TOWNSHIP/MN",
      "PELI", "PELICAN RAPIDS/MN",
      "PERL", "PERLEY/MN",
      "PILL", "PILLSBURY/ND",
      "PRAI", "PRAIRIE ROSE/ND",
      "PROS", "PROSPER/ND",
      "REIL", "REILE'S ACRES/ND",
      "RIVE", "RIVERTON TOWNSHIP/MN",
      "ROGE", "ROGERS/ND",
      "ROLL", "ROLLAG/MN",
      "RUST", "RUSTAD CITY/ND",
      "SABI", "SABIN/MN",
      "SHEL", "SHELDON/ND",
      "SKRE", "SKREE TOWNSHIP/MN",
      "SPRI", "SPRING PRAIRIETOWNSHIP/MN",
      "TANS", "TANSEM TOWNSHIP/MN",
      "TOWE", "TOWER CITY/ND",
      "TWIN", "TWIN VALLEY/MN",
      "ULEN", "ULEN/MN",
      "VALL", "VALLEY CITY/ND",
      "VIDI", "VIDING TOWNSHIP/MN",
      "WARR", "WARREN/MN",
      "WFGO", "WEST FARGO/ND",
      "WHEA", "WHEATLAND/ND",   // Changed from MN
      "WILD", "WILD RICE/ND",
      "WOLV", "WOLVERTON/MN",
      
      "CHRISTINE",      "CHRISTINE/ND",
      
      "BECKCO",          "BECKER COUNTY/MN",
      "NORMCO",          "NORMAN COUNTY/MN",
      "RICHCO",          "RICHLAND COUNTY/ND",
      "ROTHSAY",         "ROTHSAY/MN",
      "TRAIL",           "TRAILL COUNTY/ND",
      "WALCOT",          "WALCOTT/ND",
      "WALCOTT",         "WALCOTT/ND",
      "WALCOT RICHCO",   "WALCOTT/ND",
      "WALCOTT RICHCO",  "WALCOTT/ND",
      "RICHCO WALCOT",   "WALCOTT/ND",
      "RICHCO WALCOTT",  "WALCOTT/ND",
      "WILKCO",          "WILKIN COUNTY/MN",
      "WILKCO ROTHSAY",  "ROTHSAY/MN",
      "WILKIN",          "WILKIN COUNTY/MN",
      "WOLVERTON",       "WOLVERTON/MN"
      

  });
}
