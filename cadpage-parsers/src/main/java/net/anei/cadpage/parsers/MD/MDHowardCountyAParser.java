package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;



public class MDHowardCountyAParser extends FieldProgramParser {

  public MDHowardCountyAParser() {
    super(CITY_CODES, "HOWARD COUNTY", "MD",
           "ADDR/S! TYPE:CALL! BEAT/BOX:BOX Disp:UNIT");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net,dwalton@howardcountymd.gov,hc1@howardcountymd.gov,@iamresponding.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 240; }
      @Override public int splitBreakPad() { return 1; }
      @Override public boolean mixedMsgOrder() { return true; }
    };
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    body = body.replace("=20", " ").trim();
    do {
      if (subject.startsWith("CAD") || subject.startsWith("hCAD")) break;

      if (subject.startsWith("Station ")) {
        data.strSource = subject;
        break;
      }
      if (body.startsWith("EVENT: ")) break;

      if (isPositiveId()) break;
      return false;
    } while (false);

    if (!super.parseMsg(body, data)) return false;
    if (data.strUnit.length() == 0) {
      String unit = UNIT_CODES.getProperty(data.strCall);
      if (unit != null) data.strUnit = unit;
    }

    if (!data.strCallId.isEmpty()) {
      data.strInfoURL = "https://cad.howardcountymd.gov/details/" + data.strCallId;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram() + " URL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("BOX")) return new  MyBoxField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      while (field.startsWith("EVENT: ")) {
        field = field.substring(7).trim();
        int pt = field.indexOf(' ');
        if (pt < 0) {
          data.strCallId = field;
          return;
        }
        data.strCallId = field.substring(0,pt);
        field = field.substring(pt+1).trim();
      }
      field = stripFieldStart(field, "Location:");
      Parser p = new Parser(field);
      String sAddr = p.get(": @");
      data.strPlace = p.get().replace(": @", " - ");
      if (!sAddr.endsWith(")")) {
        p = new Parser(sAddr);
        p.getLastOptional(':');
        data.strApt = p.getLastOptional(',');
        sAddr = p.get();
      }
      super.parse(sAddr, data);
    }

    @Override
    public String getFieldNames() {
      return "ID " + super.getFieldNames() + " APT PLACE";
    }
  }

  private static final Pattern TRAIL_JUNK_PTN = Pattern.compile("-\\**$");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String call = p.get('@');
      Matcher match = TRAIL_JUNK_PTN.matcher(call);
      if (match.find()) call = call.substring(0,match.start()).trim();
      data.strCall =  call;

      data.strTime = p.get(' ');

      String extra = p.get();
      if (data.strAddress.length() == 0) {
        Result res = parseAddress(StartType.START_ADDR, extra);
        if (res.isValid()) {
          res.getData(data);
          extra = res.getLeft();
          if (extra.startsWith("-"))  extra = extra.substring(1).trim();
        }
        data.strSupp = extra;
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL UNIT TIME";
    }
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        String extra = field.substring(pt+1).trim();
        field = field.substring(0,pt);

        extra = stripFieldEnd(extra, " --");
        String alarm = ALARM_CODES.getProperty(extra);
        if (alarm != null) {
          data.strSupp = append(alarm, "\n", data.strSupp);
        } else {
          data.strSupp = append(data.strSupp, "\n", extra);
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "BOX INFO";
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return NS_PTN.matcher(sAddress).replaceAll("");
  }
  private static Pattern NS_PTN = Pattern.compile("\\bNS\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties ALARM_CODES = buildCodeTable(new String[]{
      "Alarm Level 0", "1st Alarm",
      "Alarm Level 1", "Task Force",
      "Alarm Level 2", "2nd Alarm",
      "Alarm level 3", "3rd Alarm"
  });

  private static final Properties UNIT_CODES = buildCodeTable(new String[]{

      "AIR-LARGE",              "BX_ALM",
      "AIR-SMALL",              "BX_ALM",
      "APPLIANCE-APPLIANCE",    "BX_ALM",
      "FIRE-BARN",              "BX_ALM",
      "FIRE-BUSINESS",          "BX_ALM",
      "FIRE-HEALTHCARE",        "BX_ALM",
      "FIRE-HIGHOCC/ASSEMBLY",  "BX_ALM",
      "FIRE-HIGHRISE",          "BX_ALM",
      "FIRE-HOUSE",             "BX_ALM",
      "FIRE-JAIL",              "BX_ALM",
      "FIRE-MULTIFAMILY",       "BX_ALM",
      "FIRE-OUTBLDG",           "BX_ALM",
      "GASLEAK-INSIDE/HIGHOCC", "BX_ALM",
      "GASLEAK-INSIDE/HOUSE",   "BX_ALM",
      "INVEST-ELECTRICAL",      "BX_ALM",
      "INVEST-FIRE/OUT",        "BX_ALM",
      "LANDFILL-LANDFILL",      "BX_ALM",
      "ODOR-SMOKE/INSIDE",      "BX_ALM",
      "RAIL-CARFIRE",           "BX_ALM",
      "RAIL-FREIGHT",           "BX_ALM",
      "RAIL-PASSNGR",           "BX_ALM",
      "RAIL-TANKFIRE",          "BX_ALM",
      "RAIL-TANKLEAK",          "BX_ALM",
      "SMOKE-HIGHRISE",         "BX_ALM",
      "SMOKE-INSIDE/HIGHOCC",   "BX_ALM",
      "SMOKE-INSIDE/HOUSE",     "BX_ALM",

      "BUS-UNKNOWN",            "SER_ACC",
      "RAIL-PED",               "SER_ACC",
      "RAIL-VSAUTO",            "SER_ACC",
      "RESCUE-AUTOVS",          "SER_ACC",
      "RESCUE-BARIATRIC",       "SER_ACC",
      "RESCUE-EJECTED",         "SER_ACC",
      "RESCUE-HAZMAT",          "SER_ACC",
      "RESCUE-HEAVYRESCUE",     "SER_ACC",
      "RESCUE-INJURIES",        "SER_ACC",
      "RESCUE-MACHINERY",       "SER_ACC",
      "RESCUE-MASCAS",          "SER_ACC",
      "RESCUE-MULTIPLE",        "SER_ACC",
      "RESCUE-NOTALERT",        "SER_ACC",
      "RESCUE-RESFIRE",         "SER_ACC",
      "RESCUE-TRAPPED",         "SER_ACC",

      "CAVEIN-CAVEIN",          "SPEC_OPS",
      "COLLAPSE-COLLAPSE",      "SPEC_OPS",
      "COLLAPSE-VEHICLE",       "SPEC_OPS",
      "CONFINED-CONFINED",      "SPEC_OPS",
      "HAZMAT-HAZMATF",         "SPEC_OPS",
      "HAZMAT-HAZNO",           "SPEC_OPS",
      "HAZMAT-NOFIRE",          "SPEC_OPS",
      "INVPKG",                 "SPEC_OPS",
      "TECHRES-TECHRES",        "SPEC_OPS",

      "ALFIRE-BUSC",            "MISC",
      "ALFIRE-RES",             "MISC",
      "BRUSH-BRUSH",            "MISC",
      "CO-NOSYMP",              "MISC",
      "CO-SYMPTOMS",            "MISC",
      "GASLEAK-OUTSIDE",        "MISC",
      "LOCKR-LOCKR",            "MISC",
      "LOCKV-ANIMAL",           "MISC",
      "MISC-default",           "MISC",
      "MUTAID-MAFIRE",          "MISC",
      "ODOR-OUTSIDE",           "MISC",
      "RESCUE-UNKNOWN",         "MISC",
      "TRANSFER-ENGINE",        "MISC",
      "VEHICLE-AUTO",           "MISC",
      "VEHICLE-TRUCK",          "MISC"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANNJ", "ANNAPOLIS JUNCTION",
      "BWI",  "BWI AIRPORT",
      "CLK",  "CLARKSVILLE",
      "COOK", "COOKSVILLE",
      "DYTN", "DAYTON",
      "EC",   "ELLICOTT CITY",
      "ECOL", "COLUMBIA",
      "ECW",  "ELLICOTT CITY",
      "ELK",  "ELKRIDGE",
      "FULT", "FULTON",
      "GLNG", "GLENELG",
      "GLWD", "GLENWOOD",
      "HANR", "HANOVER",
      "HIGH", "HIGHLAND",
      "JSSP", "JESSUP",
      "LAUR", "LAUREL",
      "MARR", "MARRIOTTSVILLE",
      "MTAR", "MOUNT AIRY",
      "SAVG", "SAVAGE",
      "SCOL", "COLUMBIA",
      "SYKE", "SYKESVILLE",
      "WCOL", "COLUMBIA",
      "WDBN", "WOODBINE",
      "WDSK", "WOODSTOCK",
      "WF",   "WEST FRIENDSHIP",

  });
}
