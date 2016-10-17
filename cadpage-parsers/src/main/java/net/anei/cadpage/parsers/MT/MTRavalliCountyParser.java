package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Ravalli County, MT
 */

public class MTRavalliCountyParser extends FieldProgramParser {

  private static final Pattern BODY_SPLIT_PTN = Pattern.compile("[,;]");
  private static final Pattern UNIT2_VALIDATE_PTN = Pattern.compile("[^ ]*");
  private static final Pattern RESPOND_TO_PTN = Pattern.compile("(.*)\\bRESPOND TO +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_PTN = Pattern.compile("(.*?)(?<!STAGE |STAGING |ON CALL )\\bFOR +(?:AN? +)?(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern X_VALIDATE_PTN = Pattern.compile("(.*)(?:CROSS ST(?:REET)?|XSTREET) +(.*)", Pattern.CASE_INSENSITIVE);
  
  public MTRavalliCountyParser() {
    super(CITY_LIST, "RAVALLI COUNTY", "MT",
      "( UNIT1 UNIT2+? | ) ADDRCALLX/S CITY? X+? CALL? X+? INFOX+");
  }

  public String getFilter() {
    return "911@rc.mt.gov,2082524569";
  }

  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "RAVALLI COUNTY:");
    body = stripFieldEnd(body, "\nUNSUBSCRIBE");
    if (!super.parseFields(body.split(BODY_SPLIT_PTN.pattern()), data)) return false;
    return data.strAddress.length() > 0 && data.strCall.length() > 0;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT1")) return new MyUnit1Field();
    if (name.equals("UNIT2")) return new MyUnit2Field();
    if (name.equals("ADDRCALLX")) return new MyAddressCallXField();
    if (name.equals("X")) return new MyXField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFOX")) return new MyInfoXField();
    return super.getField(name);
  }
  
  private class MyUnit1Field extends MyUnitField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (RESPOND_TO_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }
  
  private class MyUnit2Field extends MyUnitField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!UNIT2_VALIDATE_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  private class MyAddressCallXField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = RESPOND_TO_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = append(data.strUnit, " ", match.group(1).trim());
        field = match.group(2).trim();
        match = CALL_PTN.matcher(field);
        if (match.matches()) { // check for the call pattern
          data.strCall = match.group(2).trim();
          field = match.group(1).trim();
          match = X_VALIDATE_PTN.matcher(data.strCall);
          if (match.matches()) { // if call ptn is found check cross pattern in call
            data.strCross = match.group(2).trim();
            data.strCall = match.group(1).trim();
          }
        }
      }
      match = X_VALIDATE_PTN.matcher(field);
      if (match.matches()) { // check for cross in the address
        data.strCross = append(data.strCross, " & ", match.group(2).trim());
        field = match.group(1).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT " + super.getFieldNames() + " CALL X";
    }
  }  
  
  private class MyCallField extends CallField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCall.length() != 0) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (match.matches()) {
        String city = match.group(1).trim();
        if (isCity(city)) {
          data.strCity = city;
          field = match.group(2).trim();
        } else {
          if (city.split(" +").length < 4) {
            data.strPlace = city;
            field = match.group(2).trim();
          }
        }
      }
      data.strCall = field.trim();
    }
  }
  
  private class MyXField extends CrossField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = X_VALIDATE_PTN.matcher(field);
      if (!match.matches()) return false;
      field = match.group(2).trim();
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyInfoXField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = X_VALIDATE_PTN.matcher(field);
      if (match.matches()) {
        data.strCross = append(data.strCross, " & ", match.group(2)).trim();
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return BEAR_CR_PTN.matcher(addr).replaceAll("BEAR CREEK");
  }
  private static final Pattern BEAR_CR_PTN = Pattern.compile("\\bBEAR +CR\\b");
  
  
  private static final String[] CITY_LIST = new String[]{
    "RAVALLI COUNTY",
    
    //CITIES
    "HAMILTON",
    
    //TOWNS
    "DARBY",
    "PINESDALE",
    "STEVENSVILLE",
    
    //CENSUS-DESIGNATED PLACES
    "CHARLOS HEIGHTS",
    "CONNER",
    "CORVALLIS",
    "FLORENCE",
    "SULA",
    "VICTOR"
  };
}