package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COEagleCountyParser extends FieldProgramParser {
  public COEagleCountyParser() {
    super(CITY_TABLE, "EAGLE COUNTY", "CO",
          "ADDR1/Z+? ADDR2/ZS! EVENT:CALL! TIME:TIME! Disp:UNIT!");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "ipage@vailgov.com";
  }

  private static final Pattern PAMA_PATTERN
    = Pattern.compile("(?:U\\.? *S\\.? +)?HIGHWAY +& +(\\d+)");
  @Override
  public String postAdjustMapAddress(String a) {
    Matcher m=PAMA_PATTERN.matcher(a);
    if (m.matches()) return "US-"+m.group(1);
    return a;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("IPS I/Page Notification")) return false;
    if (!body.startsWith("Loc:")) return false;
    body = body.substring(4).trim();
    
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new Address1Field();
    if (name.equals("ADDR2")) return new Address2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class Address1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, "/", field);
    }
  }
  
  private static final Pattern SPEC_PATTERN = Pattern.compile("(?:alias |@)(.*)");
  private static final Pattern MM_PTN = Pattern.compile("[NESW]B\\d+");
  private class Address2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, "/", field);
      data.strAddress = "";
      String place;
      Parser p = new Parser(field);
      while ((place = p.getLastOptional(": ")).length() > 0) {
        parseSpec(place, data);
      }
      String apt = "";
      if (!field.startsWith("LL(")) {
        apt = p.getLastOptional(';');
        apt = append(p.getLastOptional(','), "-", apt);
      }
      field = p.get().replace(" CNTY", " ").trim();
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
      if (!data.strPlace.isEmpty() && MM_PTN.matcher(data.strPlace).matches()) {
        data.strAddress = stripFieldEnd(data.strAddress, data.strPlace.substring(0,2));
        data.strAddress = append(data.strAddress, " ", data.strPlace);
        data.strPlace = "";
      }
    }
    
    private void parseSpec(String field, Data data) {
      Matcher m=SPEC_PATTERN.matcher(field);
      if (m.matches()) field = m.group(1).trim();
      data.strPlace = append(field, " - ", data.strPlace);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" PLACE";
    }
  }

  private static final Pattern CALL_PATTERN
    = Pattern.compile("(.*)- +(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher m=CALL_PATTERN.matcher(field);
      if (m.matches()) {
        data.strCall=m.group(1).trim();
        data.strCode=m.group(2);
      }
      else
        data.strCall=field;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" CODE";
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE); 
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ARROWHEAD",    "EDWARDS",
      "CORD",         "STEAMBOAT SPRINGS",
      "LAKE CREEK",   "EDWARDS",
  });
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "INTERSTATE 70 EB116",                  "+39.553512,-107.333160",
      "INTERSTATE 70 EB117",                  "+39.551493,-107.318292",
      "INTERSTATE 70 EB118",                  "+39.561564,-107.305389",
      "INTERSTATE 70 EB119",                  "+39.561319,-107.286336",
      "INTERSTATE 70 EB120",                  "+39.566254,-107.270500",
      "INTERSTATE 70 EB121",                  "+39.561301,-107.253602",
      "INTERSTATE 70 EB122",                  "+39.561711,-107.238597",
      "INTERSTATE 70 EB123",                  "+39.571515,-107.224094",
      "INTERSTATE 70 EB124",                  "+39.576327,-107.210071",
      "INTERSTATE 70 EB125",                  "+39.586390,-107.196700",
      "INTERSTATE 70 EB126",                  "+39.592477,-107.178752",
      "INTERSTATE 70 EB127",                  "+39.601769,-107.164409",
      "INTERSTATE 70 EB128",                  "+39.609695,-107.149545",
      "INTERSTATE 70 EB129",                  "+39.616170,-107.132550",
      "INTERSTATE 70 EB130",                  "+39.624347,-107.118446",
      "INTERSTATE 70 EB131",                  "+39.629004,-107.103137",
      "INTERSTATE 70 EB132",                  "+39.638463,-107.090281",
      "INTERSTATE 70 EB133",                  "+39.645787,-107.074448",
      "INTERSTATE 70 EB134",                  "+39.648584,-107.056171",
      "INTERSTATE 70 EB135",                  "+39.646211,-107.037695",
      "INTERSTATE 70 EB136",                  "+39.646150,-107.019571",
      "INTERSTATE 70 EB137",                  "+39.648924,-107.001379",
      "INTERSTATE 70 EB138",                  "+39.653687,-106.984043",
      "INTERSTATE 70 EB139",                  "+39.654102,-106.965284",
      "INTERSTATE 70 EB140",                  "+39.653125,-106.946892",
      "INTERSTATE 70 EB141",                  "+39.653866,-106.927839",
      "INTERSTATE 70 EB142",                  "+39.654231,-106.908888",
      "INTERSTATE 70 EB143",                  "+39.652565,-106.890892",
      "INTERSTATE 70 EB144",                  "+39.653133,-106.872017",
      "INTERSTATE 70 EB145",                  "+39.651747,-106.853280",
      "INTERSTATE 70 EB146",                  "+39.654863,-106.835505",
      "INTERSTATE 70 EB147",                  "+39.664146,-106.820714",
      "INTERSTATE 70 EB148",                  "+39.671085,-106.803846",
      "INTERSTATE 70 EB149",                  "+39.675264,-106.785625",
      "INTERSTATE 70 EB150",                  "+39.680139,-106.768761",
      "INTERSTATE 70 EB151",                  "+39.687250,-106.752556",
      "INTERSTATE 70 EB152",                  "+39.697487,-106.739165",
      "INTERSTATE 70 EB153",                  "+39.706188,-106.723991",
      "INTERSTATE 70 EB154",                  "+39.711178,-106.706659",
      "INTERSTATE 70 EB155",                  "+39.708005,-106.691656",
      "INTERSTATE 70 EB156",                  "+39.700587,-106.679742",
      "INTERSTATE 70 EB157",                  "+39.693414,-106.665241",
      "INTERSTATE 70 EB158",                  "+39.682845,-106.655461",
      "INTERSTATE 70 EB159",                  "+39.672807,-106.646176",
      "INTERSTATE 70 EB160",                  "+39.659864,-106.637752",
      "INTERSTATE 70 EB161",                  "+39.656197,-106.619998",
      "INTERSTATE 70 EB162",                  "+39.654187,-106.601192",
      "INTERSTATE 70 EB163",                  "+39.648015,-106.583829",
      "INTERSTATE 70 EB164",                  "+39.642755,-106.567259",
      "INTERSTATE 70 EB165",                  "+39.639435,-106.549602",
      "INTERSTATE 70 EB166",                  "+39.640472,-106.531916",
      "INTERSTATE 70 EB167",                  "+39.634138,-106.515095",
      "INTERSTATE 70 EB168",                  "+39.628718,-106.496868",
      "INTERSTATE 70 EB169",                  "+39.621350,-106.480810",
      "INTERSTATE 70 EB170",                  "+39.617486,-106.462757",
      "INTERSTATE 70 EB171",                  "+39.608369,-106.450579",
      "INTERSTATE 70 EB172",                  "+39.616195,-106.437754",
      "INTERSTATE 70 EB173",                  "+39.624500,-106.424351",
      "INTERSTATE 70 EB174",                  "+39.634288,-106.411028",
      "INTERSTATE 70 EB175",                  "+39.642907,-106.395881",
      "INTERSTATE 70 EB176",                  "+39.644315,-106.378207",
      "INTERSTATE 70 EB177",                  "+39.641437,-106.359593",
      "INTERSTATE 70 EB178",                  "+39.644378,-106.341027",
      "INTERSTATE 70 EB179",                  "+39.647206,-106.322665",
      "INTERSTATE 70 EB180",                  "+39.643562,-106.305603",
      "INTERSTATE 70 WB116",                  "+39.553512,-107.333160",
      "INTERSTATE 70 WB117",                  "+39.551493,-107.318292",
      "INTERSTATE 70 WB118",                  "+39.561564,-107.305389",
      "INTERSTATE 70 WB119",                  "+39.561319,-107.286336",
      "INTERSTATE 70 WB120",                  "+39.566254,-107.270500",
      "INTERSTATE 70 WB121",                  "+39.561301,-107.253602",
      "INTERSTATE 70 WB122",                  "+39.561711,-107.238597",
      "INTERSTATE 70 WB123",                  "+39.571515,-107.224094",
      "INTERSTATE 70 WB124",                  "+39.576327,-107.210071",
      "INTERSTATE 70 WB125",                  "+39.586390,-107.196700",
      "INTERSTATE 70 WB126",                  "+39.592477,-107.178752",
      "INTERSTATE 70 WB127",                  "+39.601769,-107.164409",
      "INTERSTATE 70 WB128",                  "+39.609695,-107.149545",
      "INTERSTATE 70 WB129",                  "+39.616170,-107.132550",
      "INTERSTATE 70 WB130",                  "+39.624347,-107.118446",
      "INTERSTATE 70 WB131",                  "+39.629004,-107.103137",
      "INTERSTATE 70 WB132",                  "+39.638463,-107.090281",
      "INTERSTATE 70 WB133",                  "+39.645787,-107.074448",
      "INTERSTATE 70 WB134",                  "+39.648584,-107.056171",
      "INTERSTATE 70 WB135",                  "+39.646211,-107.037695",
      "INTERSTATE 70 WB136",                  "+39.646150,-107.019571",
      "INTERSTATE 70 WB137",                  "+39.648924,-107.001379",
      "INTERSTATE 70 WB138",                  "+39.653687,-106.984043",
      "INTERSTATE 70 WB139",                  "+39.654102,-106.965284",
      "INTERSTATE 70 WB140",                  "+39.653125,-106.946892",
      "INTERSTATE 70 WB141",                  "+39.653866,-106.927839",
      "INTERSTATE 70 WB142",                  "+39.654231,-106.908888",
      "INTERSTATE 70 WB143",                  "+39.652565,-106.890892",
      "INTERSTATE 70 WB144",                  "+39.653133,-106.872017",
      "INTERSTATE 70 WB145",                  "+39.651747,-106.853280",
      "INTERSTATE 70 WB146",                  "+39.654863,-106.835505",
      "INTERSTATE 70 WB147",                  "+39.664146,-106.820714",
      "INTERSTATE 70 WB148",                  "+39.671085,-106.803846",
      "INTERSTATE 70 WB149",                  "+39.675264,-106.785625",
      "INTERSTATE 70 WB150",                  "+39.680139,-106.768761",
      "INTERSTATE 70 WB151",                  "+39.687250,-106.752556",
      "INTERSTATE 70 WB152",                  "+39.697487,-106.739165",
      "INTERSTATE 70 WB153",                  "+39.706188,-106.723991",
      "INTERSTATE 70 WB154",                  "+39.711178,-106.706659",
      "INTERSTATE 70 WB155",                  "+39.708005,-106.691656",
      "INTERSTATE 70 WB156",                  "+39.700587,-106.679742",
      "INTERSTATE 70 WB157",                  "+39.693414,-106.665241",
      "INTERSTATE 70 WB158",                  "+39.682845,-106.655461",
      "INTERSTATE 70 WB159",                  "+39.672807,-106.646176",
      "INTERSTATE 70 WB160",                  "+39.659864,-106.637752",
      "INTERSTATE 70 WB161",                  "+39.656197,-106.619998",
      "INTERSTATE 70 WB162",                  "+39.654187,-106.601192",
      "INTERSTATE 70 WB163",                  "+39.648015,-106.583829",
      "INTERSTATE 70 WB164",                  "+39.642755,-106.567259",
      "INTERSTATE 70 WB165",                  "+39.639435,-106.549602",
      "INTERSTATE 70 WB166",                  "+39.640472,-106.531916",
      "INTERSTATE 70 WB167",                  "+39.634138,-106.515095",
      "INTERSTATE 70 WB168",                  "+39.628718,-106.496868",
      "INTERSTATE 70 WB169",                  "+39.621350,-106.480810",
      "INTERSTATE 70 WB170",                  "+39.617486,-106.462757",
      "INTERSTATE 70 WB171",                  "+39.608369,-106.450579",
      "INTERSTATE 70 WB172",                  "+39.616195,-106.437754",
      "INTERSTATE 70 WB173",                  "+39.624500,-106.424351",
      "INTERSTATE 70 WB174",                  "+39.634288,-106.411028",
      "INTERSTATE 70 WB175",                  "+39.642907,-106.395881",
      "INTERSTATE 70 WB176",                  "+39.644315,-106.378207",
      "INTERSTATE 70 WB177",                  "+39.641437,-106.359593",
      "INTERSTATE 70 WB178",                  "+39.644378,-106.341027",
      "INTERSTATE 70 WB179",                  "+39.647206,-106.322665",
      "INTERSTATE 70 WB180",                  "+39.643562,-106.305603"
  });
  
  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
      
//      "","GRAND COUNTY",
//      "","SUMMIT COUNTY",
//      "","LAKE COUNTY",
//      "","PITKIN COUNTY",
//      "","GARFIELD COUNTY",
//      "","ROUTT COUNTY",
      "ARW",  "ARROWHEAD",
      "AVON", "AVON",
//      "", "BASALT",
      "BATG", "BACHELOR GULCH",
      "BCK",  "BEAVER CREEK",
      "BON",  "BOND",
      "BUR",  "BURNS",
      "CORD", "CORD",   // ???
      "CORR", "EDWARDS",
      "DOT",  "",
      "EAGL", "EAGLE",
      "EGV",  "EAGLE-VAIL",
      "EDWD", "EDWARDS",
//      "", "EL JEBEL",
      "GYPS", "GYPSUM",
      "LKCK", "LAKE CREEK",
      "MINT", "MINTURN",
      "REDC", "RED CLIFF",
      "VAIL", "VAIL",
      "WOLC", "WOLCOTT",
//      "", "GILMAN",

  });
}
