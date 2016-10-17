package net.anei.cadpage.parsers.AK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Dothan AL
 */
public class AKKenaiPeninsulaBoroughParser extends FieldProgramParser {
  
  
  public AKKenaiPeninsulaBoroughParser() {
    super("KENAI PENINSULA BOROUGH", "AK",
           "Location:EMPTY ADDR CITY? ST Cross_Streets:X! X+ Call_Type:CALL! CALL INFO+ Dispatch_Code:CODE CODE");
  }
  
  @Override
  public String getFilter() {
    return "Disp-CES@borough.kenai.ak.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("911 Rip And Run Service")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  private static final Pattern ADDR_PHONE_PTN = Pattern.compile(" (\\d{7})$");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PHONE_PTN.matcher(field);
      if (match.find()) {
        data.strPhone = match.group(1);
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PHONE";
    }
  }

  private static final Pattern CROSS_DELIM = Pattern.compile(" *[&/] *");
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      for (String part : CROSS_DELIM.split(field)) {
        if (part.length() == 0) continue;
        if (part.equals("Unknown")) continue;
        if (!data.strCross.contains(part)) {
          data.strCross = append(data.strCross, " / ", part);
        }
      }
    }
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("^(\\d+)-"); 
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.find()) {
        data.strCode = match.group(1);
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String sep = (data.strSupp.endsWith(":") ? " " : "\n");
      data.strSupp = append(data.strSupp, sep, field);
    }
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.equals("Unknown")) return;
      if (field.equals("--")) return;
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ST")) return new SkipField(", AK");
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CODE")) return new MyCodeField();
    return super.getField(name);
  }
}
