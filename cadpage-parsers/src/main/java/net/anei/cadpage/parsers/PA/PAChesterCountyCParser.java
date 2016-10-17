package net.anei.cadpage.parsers.PA;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;




public class PAChesterCountyCParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyCParser() {
    super("CALL ADDRPL! X2? APT? CITY? TIME! INFO+");
  }

  // Delimiter pattern is start that is not followed by a second star
  private static final Pattern STAR_DELIM = Pattern.compile("\\*(?!\\*)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    String fields[] = STAR_DELIM.split(body);
    if (fields.length < 4) return false;
    return parseFields(fields, data);
  }
} 
