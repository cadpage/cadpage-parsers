package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchX01Parser;

public class MDDorchesterCountyBParser extends DispatchX01Parser {

  public MDDorchesterCountyBParser() {
    super(MDDorchesterCountyParser.CITY_CODES, "DORCHESTER COUNTY", "MD");
    setupMapAdjustReplacements(MDDorchesterCountyParser.MAP_ADJ_TABLE);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MARKER = Pattern.compile("Dorchester[ A-Za-z]*: *");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end());
    return super.parseHtmlMsg(subject, body, data);
  }
}
