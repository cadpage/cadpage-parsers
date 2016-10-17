package net.anei.cadpage.parsers.ZAU;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ZAUMountBarkerParser extends SmartAddressParser {
  public ZAUMountBarkerParser() {
    super(CITY_LIST, "MOUNT BARKER", "SA", CountryCode.AU);
    setFieldList("ID SRC DATE TIME UNIT CALL PRI PLACE ADDR CITY MAP CH INFO UNIT");
    setupMultiWordStreets(
        "BACK CALLINGTON",
        "BALD HILLS",
        "BROOKMAN CONNOR",
        "BULL CREEK",
        "CHURCH HILL",
        "DASHWOOD GULLY",
        "MOUNT BARKER",
        "RAY ORR",
        "SOUTH EASTERN",
        "WICKHAMS HILL"
    );
    removeWords("XING");
  }

  @Override
  public String getLocName() {
    return "Mount Barker, AU";
  }

  @Override
  public String getFilter() {
    return "info@lanek.com.au"; 
  }
  
  private static final String SUBJECT_STRING = "[PDW]CFS Heysen Group Officers Response",
      DATE_HEADER_S = "\\d{2}:\\d{2} \\w{3} \\d{2}/\\d{2}/\\d{2} >",
      SOURCE_S = "([A-Z0-9]+): +",
      UNIT_S = "\\*(\\w+) (INC\\d{4}) +",
      DATE_TIME_S  = "(\\d{2}/\\d{2}/\\d{2} +\\d{2}:\\d{2})",
      CALL_S = "(.*?), ALARM LEVEL: +(\\d+),",
      ADDRESS_S = "(.*?), *MAP:([A-Z]{3} +\\w{1,4} \\w{1,4}), *TG (\\d{1,3}), *(?:==)?(.*)";
  private static final Pattern MASTER
    = Pattern.compile(DATE_HEADER_S
        + SOURCE_S
        + UNIT_S
        + DATE_TIME_S
        + CALL_S
        + ADDRESS_S);

  private static final DateFormat MY_DATE_FMT
    = new SimpleDateFormat ("dd/MM/yy HH:mm");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals(SUBJECT_STRING)) return false;
    Matcher m = MASTER.matcher(body);
    if (m.matches()) {
      data.strSource = m.group(1);
      data.strUnit = m.group(2);
      data.strCallId = m.group(3);
      setDateTime(MY_DATE_FMT, m.group(4), data);
      data.strCall = m.group(5).trim();
      data.strPriority = m.group(6);
      parseMBAddress(m.group(7).trim(), data);
      data.strMap = m.group(8).trim();
      data.strChannel = m.group(9);
      parseSupplemental(m.group(10).trim(), data);
    }
    else {
      data.strCall = "GENERAL ALERT";
      data.strPlace = body;
    }
    return true;
  }
  
  private static final Pattern ADDRESS_PATTERN
    = Pattern.compile(": @?(.*)");
  private void parseMBAddress(String field, Data data) {
    StartType st = StartType.START_ADDR;
    Matcher m = ADDRESS_PATTERN.matcher(field);
    if (m.matches()) {
      st = StartType.START_PLACE;
      field = m.group(1).trim();
    }
    parseAddress(st, FLAG_ANCHOR_END, field, data);
  }
  
  private static final Pattern UNIT_PATTERN
    = Pattern.compile("(.*):(.*?):(.*)");
  private void parseSupplemental(String field, Data data) {
    Matcher m = UNIT_PATTERN.matcher(field);
    if (m.matches()) {
      field = append(m.group(1).trim(), " ", m.group(3).trim());
      data.strUnit = m.group(2).trim();
    }
    data.strSupp = append(data.strSupp, "/", field);
  }
  
  private static final String[] CITY_LIST = {
    "BRUKUNGA",
    "BULL CREEK",
    "CLARENDON",
    "KANGARILLA",
    "MCLAREN FLAT",
    "MOUNT BARKER",
    "TOTNESS",
    "VERDUN",
    
    // Battunga Country   ---   includes the following towns:
    "CALLINGTON",
    "DAWESLEY",
    "ECHUNGA",
    "FLAXLEY",
    "HAHNDORF",
    "HARROGATE",
    "JUPITER CREEK",
    "KANMANTOO",
    "KUITPO",
    "LITTLEHAMPTON",
    "MACCLESFIELD",
    "MEADOWS",
    "NAIRNE",
    "PAECHTOWN",
    "PARIS CREEK",
    "PROSPECT HILL",
    "WISTOW",
  };
}
