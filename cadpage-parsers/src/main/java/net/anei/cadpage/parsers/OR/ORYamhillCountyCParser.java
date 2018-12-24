package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ORYamhillCountyCParser extends FieldProgramParser {
  
  public ORYamhillCountyCParser() {
    super("YAMHILL COUNTY", "OR", 
          "( SELECT/1 CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! Cross_Street:X? MAP:MAP% UNIT:UNIT% " + 
          "| CALL ADDR PLACE! Caller:NAME! Caller_#:PHONE! Units:UNIT! ) END");
  }
  
  @Override
  public String getFilter() {
    return "ycom@ycom911.org";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("\\* ?CALL TIMES ?\\* ?Run #:((?:[A-Z]+-)?[-0-9]*) +Add: *?(.*?) +?((?:Call Rec:|Call Received:|Disp:).*)");
  private static final Pattern RR_BRK_PTN = Pattern.compile("(?:[* ]+|(?<!(?:^|[* \n])))(?=(?:Disp|Enr|Onscene|Avail|Unit):)");
  private static final Pattern DELIM = Pattern.compile("\\* ");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID ADDR APT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      parseAddress(match.group(2), data);
      data.strSupp = RR_BRK_PTN.matcher(match.group(3)).replaceAll("\n");
      return true;
    }
    
    if (body.startsWith("*CALL INFORMATION*")) {
      setSelectValue("2");
      body = body.substring(18).trim();
      return parseFields(body.split(";"), data);
    }
    
    setSelectValue("1");
    body = body.replace("*Cross Street:", "* Cross Street:");
    return parseFields(DELIM.split(body), data);
  }
}
