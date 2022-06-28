package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class VARappahannockCountyParser extends FieldProgramParser {
  
  public VARappahannockCountyParser() {
    super("RAPPAHANNOCK COUNTY", "VA", 
          "CALL ADDRCITYST ID GPS! END");
  }
  
  @Override
  public String getFilter() {
    return "rcdispatch@rappahannockcountyva.gov";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
    };
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MASTER = Pattern.compile("(\\w+): +(.*)");
  private static final Pattern DELIM = Pattern.compile(" +\\| +");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    body = match.group(2);
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  

}
