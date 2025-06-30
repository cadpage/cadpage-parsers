package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class WAClarkCountyParser extends FieldProgramParser {

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("([^ ]*) *\\bMPU: *(.*)");

  public WAClarkCountyParser() {
    super(CITY_CODES, "CLARK COUNTY", "WA",
           "SRC LOC:ADDR/S? ( MAP:MAP! OPS:CALL! | ) SUB_TYPE:CODE! PRI:PRI! TIME:TIME! EV#:ID! ALARM:SKIP! Disp:UNIT!");
  }

  @Override
  public String getFilter() {
    return "777,888,CRESA CAD,ipagecresa@cresa.wa.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (body.startsWith("MIME-Version:")) {
      int pt = body.indexOf("\nLOC:");
      if (pt < 0) pt = body.indexOf("\nMAP:");
      if (pt >= 0) {
        body = cleanBody(body.substring(pt+1));
      } else {
        pt = body.indexOf("\n\n");
        if (pt > 0) {
          setFieldList("INFO");
          data.msgType = MsgType.GEN_ALERT;
          data.strSupp = cleanBody(body.substring(pt+2).trim());
          return true;
        } else {
          return false;
        }
      }
    }

    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("SRC INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSource = match.group(1);
      data.strSupp = match.group(2);
      return true;
    }

    body = body.replace(" UNITS:", " Disp:");

    if (!super.parseMsg(body, data)) return false;

    if (data.strCity.length() == 0 && data.strMap.length() > 0) {
      String city = MAP_CITY_TABLE.getProperty(data.strMap);
      if (city != null) data.strCity = city;
    }
    return true;
  }

  private static final Pattern JUNK_PTN = Pattern.compile("=(?:20)?(?:\n|$)");
  private String cleanBody(String body) {
    return JUNK_PTN.matcher(body).replaceAll("");
  }

  @Override
  public String getProgram() {
    return "CITY? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      // Next field will generally be a MAP: field.  If it is not, then this
      // is an OOC mutual aid call.  THere is not MAP or OPS: field, the
      // call description is concatenated with the address, hopefully with a
      // double blank separator, and there is no city
      if (!getRelativeField(+1).startsWith("MAP:")) {
        data.defCity = "";
        int pt = field.lastIndexOf("  ");
        if (pt >= 0) {
          data.strCall = field.substring(pt+2).trim();
          super.parse(field.substring(0,pt).trim(), data);
        } else {
          parseAddress(StartType.START_ADDR, field, data);
          data.strCall = getLeft();
          if (data.strCall.length() == 0) abort();
        }

      } else {
        Parser p = new Parser(field);
        data.strPlace = p.getLastOptional(": @");
        data.strApt = p.getLastOptional(',');
        super.parse(p.get(), data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE CALL";
    }
  }

  private static final Pattern OPS_CALL_PTN = Pattern.compile("(OPS\\d+) +(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = OPS_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH CALL";
    }
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "1301", "Camas",
      "1302", "Camas",
      "1303", "Camas",
      "1304", "Camas",
      "1305", "Camas",
      "1308", "Camas",
      "1313", "Camas",
      "1314", "Camas",
      "1315", "Camas",
      "1316", "Camas",
      "1317", "Camas",
      "1318", "Camas",
      "1401", "Washougal",
      "1402", "Washougal",
      "1403", "Washougal",
      "1404", "Washougal",
      "1405", "Washougal",
      "1406", "Washougal",
      "1407", "Washougal",
      "1408", "Washougal",
      "1409", "Washougal",
      "1410", "Washougal",
      "1411", "Washougal",
      "1412", "Washougal",
      "1413", "Washougal",
      "1414", "Washougal",
      "1415", "Washougal",
      "1416", "Washougal",
      "1417", "Washougal",
      "1418", "Washougal",
      "2311", "Camas",
      "2312", "Camas",
      "2314", "Camas",
      "2315", "Camas",
      "2321", "Camas",
      "2322", "Camas",
      "2323", "Camas",
      "2324", "Camas",
      "2325", "Camas",
      "2326", "Camas",
      "2327", "Camas",
      "2328", "Camas",
      "2332", "Camas",
      "2333", "Camas ",
      "2334", "Camas",
      "2335", "Camas",
      "2336", "Washougal",
      "2403", "Washougal",
      "2404", "Washougal",
      "2405", "Washougal",
      "2406", "Washougal",
      "2407", "Washougal",
      "2409", "Washougal",
      "2410", "Washougal",
      "2415", "Washougal",
      "2416", "Washougal",
      "2417", "Washougal",
      "2418", "Washougal",
      "2419", "Washougal",
      "2420", "Washougal",
      "2421", "Washougal",
      "2422", "Washougal",
      "2423", "Washougal",
      "2424", "Washougal",
      "2425", "Washougal",
      "2426", "Washougal",
      "2427", "Washougal",
      "2428", "Washougal",
      "2429", "Washougal",
      "2430", "Washougal",
      "2431", "Washougal",
      "2432", "Washougal",
      "2433", "Washougal",
      "2434", "Washougal",
      "2435", "Washougal",
      "2436", "Washougal"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BG",   "Battleground",
      "CAM",  "Camas",
      "CLK",  "Clark County",
      "CPD",  "Camas",
      "LCPD", "La Center",
      "RPD",  "Ridgefield",
      "VAN",  "Vancouver",
      "VPD",  "Vancouver",
      "WAS",  "Washougal",
      "WPD",  "Washougal"

  });
}
