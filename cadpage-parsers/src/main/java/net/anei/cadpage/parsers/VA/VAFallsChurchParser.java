package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Falls Church, VA
 */

public class VAFallsChurchParser extends FieldProgramParser {  
  public VAFallsChurchParser() {
    super("FALLS CHURCH", "VA",
          "EMPTY ( " +
                  "Loc:ADDR! Box:BOX! ( Ch:MY_CH Type:INFO? | INFO )" +
               " | Box:BOX! Location:ADDR! Channel:CH! Info:INFO!" +
               " | DISPATCH:DISP! ( Unit:UNIT! | Units:UNIT! ) DATETIME!" +
               " )");
    }
  
  @Override
  public String getFilter() {
    return "rwaller@arlingtonva.us";
  }
  
  private static final Pattern SUBJECT_PATTERN
    = Pattern.compile("(?:Re: +)?\\[.*?\\](.*)");
  private static final Pattern TAG_PATTERN
    = Pattern.compile("\n|(?=(?:Loc(?:ation)?|Box|Ch(?:annel)?|Info|DISPATCH|Units?|Type):)|(?<=Box: ?\\d{5} ?+)", Pattern.DOTALL);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher m = SUBJECT_PATTERN.matcher(subject);
    if (!m.matches()) return false;
    data.strCall = m.group(1).trim();
    
    int pt = body.indexOf("  https://");
    if (pt < 0) pt = body.indexOf("\n\n--");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(TAG_PATTERN.split(body), data);
  }

  @Override
  public String getProgram() { 
    return "CALL " + super.getProgram(); 
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("BOX")) return new BoxField("\\d{5}", true);
    if (name.equals("MY_CH")) return new MyChannelField();
    if (name.equals("DISP")) return new DispatchField();
    if (name.equals("DATETIME")) return new MyDateTimeField("Sent by admin +[A-Z][a-z]{2} (.*)", true);
    return super.getField(name);
  }
  
  private static final Pattern MY_ADDRESS_PATTERN
    = Pattern.compile("(.*)(?:\\(([^)]+)\\)|(?:and|at|&|/)(.*))");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (city.length() == 2) {
          data.strCity = convertCodes(city, CITY_CODES);
        } else {
          data.strPlace = city;
        }
      }
      Matcher m = MY_ADDRESS_PATTERN.matcher(field);
      if (m.matches()) {
        String part2 = append(getOptGroup(m.group(2)), " ", getOptGroup(m.group(3)));
        Result r = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, part2);
        if (!r.isValid()) {
          data.strPlace = part2.trim();
          field = m.group(1).trim();
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" PLACE CITY";
    }
  }
  
  private static final Pattern MY_CHANNEL_PATTERN
    = Pattern.compile("(\\d[A-Fa-f](?: *(?:and|&|/)? *\\d *[A-Fa-f])?)\\W*(.*)");
  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = MY_CHANNEL_PATTERN.matcher(field);
      if (m.matches()) {
        data.strChannel = m.group(1);
        field = m.group(2);
      }
      data.strSupp = append(field, "/", data.strSupp);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" INFO";
    }
  }
  
  private class DispatchField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String[] f = field.split(",");
      if (f.length != 4) abort();
      data.strCall = f[0].trim();
      data.strBox = f[1].trim();
      parseAddress(f[2].trim(), data);
      data.strCity = convertCodes(f[3].trim(), CITY_CODES);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL BOX ADDR APT CITY";
    }
  }
  
  private static final DateFormat MY_DATE_FMT
    = new SimpleDateFormat("MMM dd hh:mm:ss yyyy");
  private class MyDateTimeField extends DateTimeField {
    public MyDateTimeField(String p, boolean h) {
      super(p, h);
    }
    
    @Override
    public void parse (String field, Data data) {
      setDateTime(MY_DATE_FMT, field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AC", "ARLINGTON COUNTY",
      "FC", "FALLS CHURCH"
  });
}
