package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class MIInghamCountyAParser extends DispatchA3Parser{
  
  private static final Pattern PLACE_INFO_PTN = Pattern.compile("([ A-Z]+?) +([a-z].*)");

  public MIInghamCountyAParser() {
    super("InghamCO:", "INGHAM COUNTY", "MI", "ID ADDR APT APT CITY! Line6:X! Line7:X! Line8:MAP! Line9:INFO1! Line10:CALL! Line11:CALL! Line12:NAME Line13:PHONE Line14:UNIT Line15:MAP Line16:INFO/N Line17:INFO/N Line18:INFO/N");

  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    setBreakChar('=');
    if (!super.parseMsg(body, data)) return false;
    
    data.strSupp = data.strSupp.replace(" / ", "\n");
    
    // Landmark fields often contain upper case place name followed by lower case information
    Matcher match = PLACE_INFO_PTN.matcher(data.strPlace);
    if (match.matches()) {
      data.strPlace = match.group(1);
      data.strSupp = append(data.strSupp, "\n", match.group(2));
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d\\d\\-\\d{7}|", true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
}
