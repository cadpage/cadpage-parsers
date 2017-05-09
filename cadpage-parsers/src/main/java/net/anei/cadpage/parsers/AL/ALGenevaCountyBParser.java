
package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Baldwin County, AL
 */
public class ALGenevaCountyBParser extends FieldProgramParser {

  public ALGenevaCountyBParser() {
    super("GENEVA COUNTY", "AL",
          "CALL! Location:ADDR! City:CITY! Subject:CALL/SDS? Place:PLACE? Details:INFO!");
  }
    
  @Override
  public String getFilter() {
    return "34Central@smtp.gmail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern MARKER = Pattern.compile("34 ?Central:_ *");
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end());
    return super.parseFields(body.split(" //"), data);
  }
}
