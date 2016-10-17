package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PALackawannaCountyAParser extends FieldProgramParser {
  
  public PALackawannaCountyAParser() {
    super(CITY_CODES, "LACKAWANNA COUNTY", "PA",
          "UNIT! Location:ADDR/aSXax! Common_Name:PLACE? Call_Type:CALL! Call_Time:DATETIME Nature_of_Call:INFO CFS_Number:ID");
    addRoadSuffixTerms("CLOSE", "PARK");
    setupSpecialStreets("NEW ST");
  }
  
  @Override
  public String getFilter() {
    return "aegispage@lackawannacounty.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    body = body.replace("Call Type:", " Call Type:").replaceAll(" CFS Number ", " CFS Number: ").replace('\n', ' ');

    return super.parseMsg(body, data);
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?) +(?:(?:APT|RM|(LOT|WARD)) *([^ ]+|[^0-9]+)|(FLR +\\d+(?: +APT +.*)?))", Pattern.CASE_INSENSITIVE);
  private static final Pattern LVIH_PTN = Pattern.compile("\\bLVIH\\b");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("0 ")) field = field.substring(2).trim();
      Matcher match = ADDR_APT_PTN.matcher(field);
      String apt = "";
      if (match.matches()) {
        field = match.group(1);
        apt = match.group(4);
        if (apt == null) {
          apt = append(getOptGroup(match.group(2)), " ", match.group(3));
        }
      }
      field = LVIH_PTN.matcher(field).replaceAll("LVIH HWY");
      super.parse(field, data);
      data.strAddress = data.strAddress.replace("LVIH HWY", "LVIH");
      data.strCross = data.strCross.replace("LVIH HWY", "LVIH");
      data.strApt = append(data.strApt, "-", apt);
      if (data.strCross.equals("No Cross Streets Found")) data.strCross = "";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("Unit\\b *(.*)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return LVIH_PTN.matcher(addr).replaceAll("LACKAWANNA VALLEY IND HWY");
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "1",    "Waverly",
      "2",    "South Abington",
      "4",    "Clarks Summit",
      "5",    "Dalton",
      "6",    "Dunmore",
      "7",    "Moscow",
      "8",    "Newton Twp",
      "10",   "Clarks Green",
      "11",   "Glenburn",
      "14",   "Covington",
      "15",   "Roaring Brook",
      "16",   "Elmhurst Twp",
      "18",   "Clifford Twp",
      "20",   "Peckville",
      "21",   "Archbald",
      "22",   "Blakely",
      "23",   "Dickson City",
      "24",   "Greenfield",
      "25",   "Jessup",
      "26",   "Olyphant",
      "27",   "Throop",
      "28",   "Scott Twp",
      "29",   "Jefferson Twp",
      "33",   "Eynon",
      "34",   "Sturges",
      "35",   "Scranton",
      "36",   "",              // Definitely means something??? (Dalton)
      "41",   "Forest City",
      "42",   "Vandling",
      "43",   "Browndale",
      "50",   "Scranton",
      "51",   "Carbondale",
      "52",   "Ransom Twp",
      "53",   "Springbrook Twp",
      "54",   "Thornhurst",
      "55",   "Lehigh Twp",
      "56",   "Madison Twp",
      "58",   "Jermyn",
      "59",   "Mayfield",
      "60",   "Carbondale Twp",
      "61",   "Fell Twp",
      "63",   "Benton Twp",
      "64",   "North Abington",
      "65",   "West Abington",
      "66",   "LaPlume",
      "67",   "Moscow",
      "93",   "Old Forge",
      "94",   "Moosic",
      "95",   "Taylor",
      "98",   "Greenwood",
      
      "BENTON TWP",   "Benton Twp",
      "FALLS TWP WYOMING CNTY",  "Falls Twp, Wyoming County",
      "FALLS",        "Falls Twp, Wyoming County",
      "LAKE ARIEL",   "Lake Ariel",
      "LUZN CNTY",    "Luzerne County",
      "LUZERNE CNTY", "Luzerne County",
      "MONROE CNTY",  "Monroe County",
      "SUSQ CNTY",    "Susquehanna County",
      "WAYNE CNTY",   "Wayne County",
      "WYOMING CNTY", "Wyoming County",

      // Susquehanna County
      "LENOXVILLE",   "Lenoxville"
  });
}
