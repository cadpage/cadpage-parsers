package net.anei.cadpage.parsers.NV;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class NVClarkCountyAParser extends FieldProgramParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("RU?N#:?([A-Z]?\\d+) +(.*)");
  
  public NVClarkCountyAParser() {
    super(CITY_CODES, "CLARK COUNTY", "NV",
          "I:ID! U:UNIT! P:PRI! G:MAP! PH:MAP! L:ADDR/y! B:PLACE! AL:INFO! PC:CODE! CODE N:SKIP TIME+");
  }
  
  @Override
  public String getFilter() {
    return "sms@pageway.net,44627545";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).replace('/', '\n').trim();
      return true;
    }
    
    body = body.replace(" U:", ", U:").replace(" L:", ", L:").replace(" PC:", ", PC:").replace(" N:", ", N:");
    if (body.startsWith("RE:")) body = "I:" + body.substring(3);
    return parseFields(body.split(","), 9, data);
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "-", field);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strApt = p.getLastOptional('#');
      field = p.get();
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY APT";
    }
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("^(\\d\\d:\\d\\d:\\d\\d)\\b");
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      if (data.strTime.length() > 0) return;
      Matcher match = TIME_PTN.matcher(field);
      if (match.find()) data.strTime = match.group(1);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CC", "CLARK COUNTY",
      "LV", "LAS VEGAS"
  });
}
