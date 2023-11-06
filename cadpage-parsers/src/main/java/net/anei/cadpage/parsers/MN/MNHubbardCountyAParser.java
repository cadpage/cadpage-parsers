package net.anei.cadpage.parsers.MN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNHubbardCountyAParser extends DispatchA43Parser {
  
  public MNHubbardCountyAParser() {
    super("HUBBARD COUNTY", "MN");
  }

  private static final Pattern DIR_OF_HWY = Pattern.compile(".*\\b(?:NORTH|SOUTH|EAST|WEST) (?:OF )?HWY");
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (DIR_OF_HWY.matcher(data.strAddress).matches()) {
      data.strAddress = append(data.strAddress, " ", data.strApt);
      data.strApt = "";
    }
    return true;
  }
  
  
}
