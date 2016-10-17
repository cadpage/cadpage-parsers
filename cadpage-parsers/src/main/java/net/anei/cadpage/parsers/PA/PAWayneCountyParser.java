package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAWayneCountyParser extends FieldProgramParser {

  private static Pattern CALL_FIELD_PATTERN = Pattern.compile("([A-Z0-9 ]+?)[^A-Z0-9]*(CO\\d+) \\(\\)");
  private static Pattern UNIT_FIELD_PATTERN = Pattern.compile("(.*?) +(WC\\d+)");
  private static Pattern INFO_FIELD_PATTERN = Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4} (\\d{1,2}:\\d{1,2}:\\d{1,2})\\b +(.*)");

  public PAWayneCountyParser() {
    super("WAYNE COUNTY", "PA", "CALL! Loc:ADDR! Rcvd:TIME! Units:UNIT! Comments:INFO");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_ADD_DEFAULT_CNTY;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.contains("Times -")) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    } else {
      String subjects[] = subject.split("\\|");
      subject = subjects[subjects.length-1];
      if (subject.length() == 1) data.strPriority = subject;
      
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {

    @Override
    public void parse(String field, Data data) {
      Matcher mat = CALL_FIELD_PATTERN.matcher(field);
      if (mat.matches()) {
        data.strCall = mat.group(1).trim();
        data.strSource = mat.group(2);
      } else abort();
    }

    @Override
    public String getFieldNames() {
      return "PRI CALL SRC";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int ai = field.indexOf("[@");
      if (ai >= 0) {
        data.strPlace = field.substring(ai + 2).trim();
        super.parse(field.substring(0, ai).trim(), data);
      } else {
        super.parse(field, data);
      }

    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE";
    }

  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = UNIT_FIELD_PATTERN.matcher(field);
      if (mat.matches()) {
        data.strCallId = mat.group(2);
        super.parse(mat.group(1).trim(), data);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "UNIT ID";
    }

  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        Matcher mat = INFO_FIELD_PATTERN.matcher(line);
        if(mat.matches()) {
          //set time if it is currently empty
          if (data.strTime.length() == 0) data.strTime = mat.group(1);
          line = mat.group(2);
        }
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
