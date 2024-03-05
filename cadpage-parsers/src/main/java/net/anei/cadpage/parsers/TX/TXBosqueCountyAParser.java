package net.anei.cadpage.parsers.TX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBosqueCountyAParser extends FieldProgramParser {
  
  public TXBosqueCountyAParser() {
    super("BOSQUE COUNTY", "TX", 
          "SRC_TIME! Fr:DISPATCH! CALL! ADDRCITY! Info:INFO INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "bosque@firepage.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("=FirePage=")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC_TIME")) return new MySourceTimeField();
    if (name.equals("DISPATCH")) return new SkipField("Dispatch", true);
    if (name.equals("CALL")) return new CallField("(.*?) +reported at:", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern SRC_TIME_PTN = Pattern.compile("(.*);(\\d\\d?:\\d\\d[AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");
  private class MySourceTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = SRC_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1).trim();
      setTime(TIME_FMT, match.group(2), data);
    }
    
    @Override
    public String getFieldNames() {
      return "SRC TIME";
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripStars(field);
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripStars(field);
      super.parse(field, data);
    }
  }
  
  private static final Pattern LEAD_STAR_PTN = Pattern.compile("^[ *]+");
  private static String stripStars(String field) {
    return LEAD_STAR_PTN.matcher(field).replaceFirst("");
  }
}
