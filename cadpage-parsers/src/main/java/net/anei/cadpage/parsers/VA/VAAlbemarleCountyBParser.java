package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Albemarle County, VA (B)
 */

public class VAAlbemarleCountyBParser extends FieldProgramParser {
  
  public VAAlbemarleCountyBParser() {
    super("ALBEMARLE COUNTY", "VA",
          "SRC! INC:ID! TYP:CALL! UNITS:UNIT! AD:ADDRCITY% APT:APT% CROSS_STREETS:X% NAME:NAME% NATURE:CALL/SDS% NARRATIVE:INFO/N+ ESN:BOX% DT:DATETIME% END");
    setupProtectedNames("LEWIS AND CLARK");
  }
  
  @Override
  public String getFilter() {
    return "cad2@acuecc.org,jplumb@albemarle.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    
    // Apt is usually duplicated at end of address
    if (data.strApt.length() > 0) {
      data.strAddress = stripFieldEnd(data.strAddress, ' ' + data.strApt);
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Za-z ]+", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("[", "").replace("]", "").trim();
      if (field.startsWith("Incident not yet created")) return;
      super.parse(field, data);
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override 
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [ap]m)?)", Pattern.CASE_INSENSITIVE);
  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2).toUpperCase();
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

}
