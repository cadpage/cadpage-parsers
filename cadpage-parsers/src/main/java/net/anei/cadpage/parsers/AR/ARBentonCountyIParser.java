package net.anei.cadpage.parsers.AR;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ARBentonCountyIParser extends DispatchA71Parser {

  public ARBentonCountyIParser() {
    super("BENTON COUNTY", "AR");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Active 911")) return false;
    if (!super.parseMsg(subject, body, data)) return false;
    if (OK_CITY_SET.contains(data.strCity)) data.strState = "OK";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public int getMapFlags( ) {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Set<String> OK_CITY_SET = new HashSet<>(Arrays.asList("COLCORD", "WATTS"));

}
