package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchVisionAirParser;

/**
 * Hoke County, NC
 */
public class NCHokeCountyParser extends DispatchVisionAirParser {
  
  public NCHokeCountyParser() {
    super(new String[]{"Hoke911:", "Hoke Co 911:"}, 
          "HOKE COUNTY", "NC", 
          "ADDR APT UNK UNK X X2 MAP UNK CALL! INFO+? EXTRA");
  }
  
  @Override
  public String getFilter() {
    return "Hoke@hokecounty.org,HokeCo911@hokecounty.org,hoke911@hokecounty.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" *** ", " ** * ");
    return super.parseMsg(body, data);
  }
  
  private class MyCross2Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("**")) abort();
      super.parse(field.substring(2).trim(), data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X2")) return new MyCross2Field();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String addr, boolean cross) {
    
    // They usually include house number with cross streets, and those need to be
    // removed if we are turning a naked street into an intersection.  House numbers
    // are defined as a string of digits followed by a blank followed by at least two
    // words
    if (cross) {
      Matcher match = STRIP_NUMBER_PTN.matcher(addr);
      if (match.matches()) addr = match.group(1);
    }
    return addr;
  }
  private static final Pattern STRIP_NUMBER_PTN = Pattern.compile("\\d+ +([^ ]+ .*)");
}
