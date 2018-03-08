package net.anei.cadpage.parsers.AR;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ARSebastianCountyParser extends FieldProgramParser {
  
  public ARSebastianCountyParser() {
    super("SEBASTIAN COUNTY", "AR", 
          "CALL:CODE_CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATETIME! TIME:SKIP! UNIT:UNIT% INFO:INFO% INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@fsems.org";
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.length() >= 243 && body.length() <= 265) data.expectMore = true;
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\S+) - (.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strCall = match.group(2).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern APT_PREFIX_PTN = Pattern.compile("(?:LOT|RM|ROOM|APT|SUITE) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      Parser p = new Parser(field);
      String apt = p.getLastOptional(",");
      super.parse(p.get(), data);
      Matcher match = APT_PREFIX_PTN.matcher(apt);
      if (match.matches()) apt = match.group(1);
      data.strApt = append(data.strApt, "-", apt);
    }
  }
  
  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Priority");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
}
