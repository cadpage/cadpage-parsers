package net.anei.cadpage.parsers.AK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Fairbanks, AK
 */
public class AKFairbanksParser extends DispatchOSSIParser {
  
  
  public AKFairbanksParser() {
    super("FAIRBANKS", "AK",
           "FYI? MUTAID? ADDR CALL/SDS! CH? BOX INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ci.fairbanks.ak.us";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MUTAID")) return new CallField(".*\\b(?:MUTUAL AID|AUTO AID)\\b.*", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CH")) return new ChannelField("TAC\\d+", true);
    if (name.equals("BOX")) return new UnitField("\\d{3}", true);
    return super.getField(name);
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*)- ?(ALPHA|BRAVO|CHARLIE|DELTA?|ECHO)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        field =  match.group(1).trim();
        data.strPriority = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL PRI";
    }
  }
}
