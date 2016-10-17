package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;




public class PAChesterCountyFParser extends PAChesterCountyBaseParser {

  private static final Pattern DETAILS_TO_FOLLOW = Pattern.compile("\\r?\\nDETAILS TO FOLLOW\\b\\r?\\n?");
  private static final Pattern DELIM = Pattern.compile("(\\* )?\\*\\*");
  
  public PAChesterCountyFParser() {
    super("CALL ADDRCITY ( CITY ( PLACE_DASH X INFO+? DATE TIME | PLACE X APT ) | APT INFO CITY! PLACE X ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "cad@oxfordfire.com,paging@minquas.org,glnf@fdcms.info";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Reject any PAChesterCountyD4 alerts
    if (body.startsWith("Dispatch ** ")) return false;
    if (body.contains("** Dispatch **")) return false;

    body = stripFieldEnd(body, " *");
    body = DETAILS_TO_FOLLOW.matcher(body).replaceAll("");
    body = body.replace('\n', ' ');

    // Split and parse by asterisk delimiters
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private static final Pattern TRAILER = Pattern.compile(" *\\(N?V\\)$");
  private class MyAddressCityField extends BaseAddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TRAILER.matcher(field);
      if (match.find()) {
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      return super.checkParse(field, data);
    }
  }
} 
