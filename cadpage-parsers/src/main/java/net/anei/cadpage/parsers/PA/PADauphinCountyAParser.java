package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Dauphin County, PA
 */
public class PADauphinCountyAParser extends FieldProgramParser {

  public PADauphinCountyAParser() {
    super(CITY_CODES, "DAUPHIN COUNTY", "PA",
           "( SELECT/SPECIAL UNIT INFO/R ADDR/S4! " +
           "| LOCATION_UPDATED LOC:ADDR/S4! BOX:BOX! " +
           "| UNIT_CALL EVENT:ID? Box:BOX! Loc:ADDR/S4 XSts:X Event_Type:CALL Class:PRI Disp:UNIT " +
           ") END", FLDPROG_IGNORE_CASE);
    setupCities(CITY_LIST);

    // BLDG is a suffix???
    removeWords("BLDG");
    addRoadSuffixTerms("BLDG");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net,donotreply-911@dauphinc.org,messaging@iamresponding.com,pagermail@verizon.net,@kpru.ac.th";
  }

  private static final Pattern SRC_PTN = Pattern.compile("^(\\d{7}) +");
  private static final Pattern WEST_HANOVER_PTN = Pattern.compile("(\\d{6}) - (\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d [AP]M) - (.*) - West Hanover", Pattern.DOTALL);
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  private static final Pattern CRITICAL_PTN = Pattern.compile("([-A-Z0-9]+) CRITICAL MSG: *(.*)");
  private static final Pattern SPECIAL_PTN = Pattern.compile("([-A-Z0-9]+) +(2ND DISPATCH|AVAILABLE|ER(?: WITH \\d)?|ENROUTE IN PLACE OF \\S+|IN SERVICE|OUT OF SERVICE)(?!.*BOX:)(?:[- ]+(.*))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.startsWith("Automatic R&R Notification:")) return false;

    body = stripFieldEnd(body, "=20");

    int pt = body.indexOf("\n\n--");
    if (pt >= 0) body = body.substring(0,pt).trim();

    // Undo IAR edits :(
    if (subject.equals("Company 34")) body = body.replace(" \n", " ");

    String[] flds = subject.split("\\|");
    if (flds[flds.length-1].equals("!")) {
      if (flds.length > 1) data.strSource = flds[0];
    }

    Matcher match = WEST_HANOVER_PTN.matcher(body);
    if (match.matches()) {
      data.strSource = match.group(1);
      setDateTime(DATE_TIME_FMT, match.group(2), data);
      body = match.group(3).trim();
    }

    else {
      match = SRC_PTN.matcher(body);
      if (match.find()) {
        if (data.strSource.length() == 0) data.strSource = match.group(1);
        body = body.substring(match.end());
      }

      if (body.endsWith(" - COMPANY 33 ALERT")) {
        body = body.substring(0, body.length()-19).trim();
      }
    }

    match = CRITICAL_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strUnit = match.group(1);
      data.strSupp = match.group(2);
      return true;
    }

    body = stripFieldStart(body, "CRITICAL MSG:");

    setSelectValue("");
    match = SPECIAL_PTN.matcher(body);
    if (match.matches()) {
      setSelectValue("SPECIAL");
      return parseFields(new String[]{match.group(1), match.group(2), getOptGroup(match.group(3))}, data);
    }

    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.startsWith("ENROUTE")) data.msgType = MsgType.RUN_REPORT;
    if (data.strAddress.length() == 0) {
      data.strAddress = data.strCross.replace('/', '&');
      data.strCross = "";
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC DATE TIME " + super.getProgram().replace(" X ", " ADDR X ");
  }

  private static Pattern CALL_UNIT_PTN = Pattern.compile("^([A-Z]+[0-9][-0-9]*) +");
  private class UnitCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_UNIT_PTN.matcher(field);
      if (match.find()) {
        data.strUnit = match.group(1);
        field  = field.substring(match.end());
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("LOCATION_UPDATED")) return new CallField("LOCATION UPDATED", true);
    if (name.equals("UNIT_CALL"))  return new UnitCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern ADDR_SPLIT_PTN = Pattern.compile("[:\n]");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*),(?:APT )?(\\d+[A-Z]?|[A-Z]|[A-Z]{1,2}\\d{1,4}[A-Z]?)");
  private static final Pattern ADDR_CITY_SFX_PTN =  Pattern.compile("^(?:SCH(?:UYLKILL| [ A-Z]+ TOWNSHIP)?)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      field = stripFieldStart(field, "/");

      for (String part : ADDR_SPLIT_PTN.split(field)) {
        part = part.trim();
        if (part.length() == 0) continue;

        String apt = null;
        Matcher match = ADDR_APT_PTN.matcher(part);
        if (match.matches()) {
          part = match.group(1).trim();
          apt = match.group(2);
        }

        if (data.strAddress.length() == 0) {
          part = stripFieldStart(part, "@");
          if (data.strCity.length() > 0) part = ADDR_CITY_SFX_PTN.matcher(part).replaceFirst("");
          parseAddress(StartType.START_ADDR, FLAG_EMPTY_ADDR_OK | FLAG_ANCHOR_END, part, data);
        }

        else if (part.startsWith("APT")) {
          part = part.substring(3).trim();
          int pt = part.indexOf('@');
          if (pt >= 0) {
            data.strPlace = append(data.strPlace, " - ", part.substring(pt+1).trim());
            part = part.substring(0,pt).trim();
          }
          data.strApt = append(data.strApt, "-", part);
        } else {
          data.strPlace = append(data.strPlace, " - ", stripFieldStart(part, "@"));
        }

        if (apt != null) data.strApt = append(data.strApt, "-", apt);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE APT";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" ,", " / ");
      super.parse(field, data);
    }
  }

  private static final Pattern PRIORITY_PTN = Pattern.compile("^(\\d)\\b *");
  private class MyPriorityField extends PriorityField {
    @Override
    public void  parse(String field, Data data) {
      Matcher match = PRIORITY_PTN.matcher(field);
      if (match.find()) {
        data.strPriority = match.group(1);
        field = field.substring(match.end());
      }
      data.strSupp = field;
    }

    @Override
    public String getFieldNames() {
      return "PRI INFO";
    }
  }

  // Sometimes there is some funny stuff at the end of the message that needs to be eliminated
  private static final Pattern UNIT_PTN = Pattern.compile("^([-/A-Z0-9, ]*)");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_PTN.matcher(field);
      if (match.find()) field = match.group(1).trim();
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = new Properties();
  private static final String[] CITY_LIST;
  static {
    Set<String> citySet = new HashSet<>();
    String key = null;
    for (String val : new String[]{
      "BEB DAUP",  "BERRYSBURG",
      "CWT DAUP",  "CONEWAGO TWP",
      "DAB DAUP",  "DAUPHIN",
      "DRY DAUP",  "DERRY TWP",
      "EHT DAUP",  "EAST HANOVER TWP",
      "ELZ DAUP",  "ELIZABETHVILLE",
      "GRB DAUP",  "GRATZ",
      "HBG DAUP",  "HARRISBURG",
      "HFT DAUP",  "HALIFAX TWP",
      "HFX DAUP",  "HALIFAX",
      "HSP DAUP",  "HIGHSPIRE",
      "HUM DAUP",  "HUMMELSTOWN",
      "JFT DAUP",  "JEFFERSON TWP",
      "JKT DAUP",  "JACKSON TWP",
      "LDT DAUP",  "LONDONDERRY TWP",
      "LPT DAUP",  "LOWER PAXTON TWP",
      "LST DAUP",  "LOWER SWATARA TWP",
      "LYK DAUP",  "LYKENS",
      "LYT DAUP",  "LYKENS TWP",
      "MDT DAUP",  "MIDDLETOWN",
      "MFT DAUP",  "MIFFLIN TWP",
      "MPT DAUP",  "MIDDLE PAXTON TWP",
      "MSB DAUP",  "MILLERSBURG",
      "PAX DAUP",  "PAXTANG",
      "PEN DAUP",  "PENBROOK",
      "PLB DAUP",  "PILLOW",
      "RDT DAUP",  "REED TWP",
      "ROY DAUP",  "ROYALTON",
      "RUS DAUP",  "RUSH TWP",
      "SHT DAUP",  "SOUTH HANOVER TWP",
      "STL DAUP",  "STEELTON",
      "SUS DAUP",  "SUSQUEHANNA TWP",
      "SWT DAUP",  "SWATARA TWP",
      "UPT DAUP",  "UPPER PAXTON TWP",
      "WHT DAUP",  "WEST HANOVER TWP",
      "WIC DAUP",  "WICONISCO",
      "WIL DAUP",  "WILLIAMSTOWN",
      "WLT DAUP",  "WILLIAMS TWP",
      "WST DAUP",  "WASHINTON TWP",
      "WYT DAUP",  "WAYNE TWP",

      // Cumberland County
      "CMPH CUMB", "CAMP HILL",
      "EPEN CUMB", "EAST PENNSBORO TWP",
      "LMYN CUMB", "LEMOYNE",
      "MSXT CUMB", "MIDDLESEX TWP",
      "SVSP CUMB", "SILVER SPRING TWP",

      // Lancaster County
      "CONY LANC", "CONOY TWP",
      "EDON LANC", "EAST DONEGAL TWP",
      "EZAB LANC", "ELIZABETHTOWN",
      "MTJT LANC", "MT JOY TWP",
      "WDON LANC", "WEST DONEGAL TWP",

      // Lebanon County
      "ANVL LEBA", "ANNVILLE TWP",
      "CLNA LEBA", "CLEONA",
      "EHAN LEBA", "EAST HANOVER TWP",
      "NLON LEBA", "NORTH LONDONDERRY TWP",
      "PALM LEBA", "PALMYRA",
      "SLON LEBA", "SOUTH LONDONDERRY TWP",

      // Northumberland County
      "HNDB NORT", "HERNDON",
      "JKST NORT", "JACKSON TWP",
      "JORD NORT", "JORDAN TWP",
      "LMHY NORT", "LOWER MAHANOY TWP",
      "UMHY NORT", "UPPER MAHANOY TWP",
      "WASH NORT", "WASHINGTON TWP",

      // Perry County
      "DUNC PERR", "DUNCANNON",
      "MARY PERR", "MARYSVILLE",
      "RYET PERR", "RYE TWP",
      "WTFD PERR", "WHEATFIELD TWP",

      // Schuylkill County
      "HEGI SCKH", "HEGINS",        // Guess??
      "KLIN SCKH", "KLINGERSTOWN",  // Guess??
      "PRTR SCKH", "PORTER TWP",
      "TREM SCKH", "TREMONT"
    }) {
      if (key == null) {
        key = val;
      } else {
        CITY_CODES.put(key, val);
        int pt = key.indexOf(' ');
        if (pt >= 0) CITY_CODES.put(key.substring(0,pt), val);
        citySet.add(val);
        if (val.endsWith(" TWP")) {
          val = val.substring(0,val.length()-3) + "TOWNSHIP";
          citySet.add(val);
        }
        key = null;
      }
    }
    CITY_LIST = citySet.toArray(new String[0]);
  }
}
