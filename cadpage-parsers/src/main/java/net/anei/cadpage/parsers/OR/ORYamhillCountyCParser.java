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
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! MAP:MAP% UNIT:UNIT%");
  }
  
  @Override
  public String getFilter() {
    return "ycom@ycom911.org";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("\\* CALL TIMES \\* Run #:([A-Z]+-[-0-9]+) +Add: *(.*?) {3,}(.*)");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern DELIM = Pattern.compile("\\* ");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID ADDR APT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      parseAddress(match.group(2), data);
      data.strSupp = MSPACE_PTN.matcher(match.group(3)).replaceAll("\n");
      return true;
    }
    return parseFields(DELIM.split(body), data);
  }
}
