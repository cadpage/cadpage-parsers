package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;



public class COMemorialStarParser extends MsgParser {
  public COMemorialStarParser() {
    super("", "CO");
    setFieldList("ID SRC ADDR PLACE CALL INFO");
  }
  
  @Override
  public String getFilter() {
    return "commcenter@med-trans.net";
  }

  private static final Pattern MASTER_PATTERN
    = Pattern.compile("([0-9\\-]+)\\:([^,]+), *((?:[^,]+,){2}) *([\\d\\.]+ *nm, *[\\d\\.]+ *deg, *)?([^,]*(?:, *ED)?)(?:, *(To\\: *.*?))?(?:_\\d *of *\\d)?");
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher m = MASTER_PATTERN.matcher(body);
    if (m.matches()) {
      data.strCallId = m.group(1);
      data.strSource = m.group(2).trim();
      data.strAddress = m.group(3).trim();
      data.strPlace = getOptGroup(m.group(4)).trim();
      data.strCall = getOptGroup(m.group(5)).trim();
      data.strSupp = getOptGroup(m.group(6)).trim();
      return true;
    }
    return false;
  }
}