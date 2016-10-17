package net.anei.cadpage.parsers.WI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Waukesha County, WI
 */
public class WIWaukeshaCountyAParser extends FieldProgramParser {

  private static final Pattern IAM_PTN1 = 
      Pattern.compile(": (.*?) : ([^:]*): (.*?)(?: (\\d\\d:\\d\\d:\\d\\d))?");
  
  private static final Pattern TIME_TRIM = Pattern.compile(" +\\d\\d:[\\d:]*$");

  public WIWaukeshaCountyAParser() {
    super(CITY_CODES, "WAUKESHA COUNTY", "WI",
        "Location:ADDR/S! TYPE_CODE:CALL! TYPE_CODE:CALL! TIME:TIME%");
    setupGpsLookupTable(WIWaukeshaCountyAParserTable.buildGpsLookupTable());
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Try to reverse IAM reformatting
    Matcher match;
    if ((match = IAM_PTN1.matcher(body)).matches()) {
      body = "Location: " + match.group(1) + " TYPE CODE: " + match.group(2) + " TYPE CODE: " + match.group(3);
      String time = match.group(4);
      if (time != null) {
        body = body + " TIME: " + time;
      } else {
        match = TIME_TRIM.matcher(body);
        if (match.find()) body = body.substring(0,match.start());
      }
    }
    return super.parseMsg(body, data);
  }

  @Override
  public String getFilter() {
    return "@iaralerts.com";
  }

  private static final Pattern APT_PTN = Pattern.compile("-([-A-Z0-9]*)$");
  private static final Pattern APT_PTN2 = Pattern.compile("(?:APT|ROOM|RM|SUITE):? *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional(": @");
      if (data.strPlace.length() == 0) data.strPlace = p.getLastOptional(':');
      data.strApt = p.getLastOptional(',');
      data.strApt = append(p.getLastOptional(';'), "-", data.strApt);
      String addr = p.get();
      if (data.strApt.length() == 0) {
        Matcher match = APT_PTN.matcher(addr);
        if (match.find()) {
          data.strApt = match.group(1);
          addr = addr.substring(0,match.start()).trim();
        } else if ((match = APT_PTN2.matcher(data.strPlace)).matches()) {
          data.strApt = match.group(1);
          data.strPlace = "";
        }
      }
      super.parse(addr, data);
      
      if (data.strCity.length() == 0 && data.strPlace.length() > 0) {
        String city = OOC_CITIES.getProperty(data.strPlace);
        if (city != null) {
          data.strCity = city;
          data.strPlace = "";
        }
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " APT PLACE";
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = NW_ADDR_PTN.matcher(addr).replaceFirst("$2");
    return addr;
  }
  private static final Pattern NW_ADDR_PTN = Pattern.compile("([NESW]\\d+[NESW]) +(.*)");
  
  @Override
  protected String adjustGpsLookupAddress(String address, String apt) {
    if (apt.length() > 0) address = address + " #" + apt;
    return address;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ASHP_T", "ASHIPPUN", 
      "BGBD_V", "BIG BEND", 
      "BRKF_C", "BROOKFIELD",
      "BRKF_T", "BROOKFIELD", 
      "BUTL_V", "BUTLER", 
      "CHEN_V", "CHENEQUA",
      "DELA_C", "DELAFIELD", 
      "DELA_T", "DELAFIELD", 
      "DOUS_V", "DOUSMAN",
      "EAGL_T", "EAGLE",
      "ELMG_V", "ELM GROVE",
      "ERIN_T", "ERIN",
      "GENS_T", "GENESSEE DEPOT",
      "HART_V", "HARTLAND",
      "LANN_V", "LANNON",
      "LISB_T", "LISBON",
      "MEFL_V", "MENOMONEE FALLS",
      "MERT_T", "MERTON",
      "MERT_V", "MERTON",
      "MILWCO", "MILWAUKEE COUNTY",
      "MUKW_T", "MUKWONAGO",
      "MUKW_V", "MUKWONAGO",
      "MUSK_C", "MUSKEGO",
      "NASH_V", "NASHOTAH",
      "NBRL_C", "NEW BERLIN",
      "NRPR_V", "NORTH PRAIRIE",
      "OCOL_V", "OCONOMOWOC LAKE",
      "OCON_C", "OCONOMOWOC",
      "OCON_T", "OCONOMOWOC",
      "OTTW_T", "OTTAWA",
      "PWKE_C", "PEWAUKEE",
      "PWKE_V", "PEWAUKEE",
      "RICH_V", "RICHFIELD",
      "SUMM_V", "SUMMIT",
      "SUSX_V", "SUSSEX",
      "VERN_T", "VERNON",
      "WALE_V", "WALES",
      "WAUK_C", "WAUKESHA",
      "WAUK_T", "WAUKESHA",
      "WAUW_C", "WAUWATOSA"
  });
  
  private static final Properties OOC_CITIES = buildCodeTable(new String[]{
      "GERMANTOWN",      "GERMANTOWN",
      "TOWN OF ERIN",    "ERIN"
  });
}