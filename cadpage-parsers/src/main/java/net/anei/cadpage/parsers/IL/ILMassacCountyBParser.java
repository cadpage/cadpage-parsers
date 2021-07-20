package net.anei.cadpage.parsers.IL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class ILMassacCountyBParser extends DispatchA29Parser {

  public ILMassacCountyBParser() {
    super(CITY_LIST,"MASSAC COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@MetropolisPD.com";
  }

  private static final Pattern UNIT_PTN = Pattern.compile("\\b(ENGINE|MEDIC|RESCUE|SQUAD) (\\d+)\\b");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    body = "DISPATCH:" + UNIT_PTN.matcher(body).replaceAll("$1_$2");
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{
    // Cities
    "BROOKPORT",
    "METROPOLIS",

    // Village
    "JOPPA",

    // Unincorporated communities
    "BARGERVILLE",
    "NEW COLUMBIA",
    "ROUND KNOB",
    "SHADY GROVE",
    "UNIONVILLE"
  };
}
