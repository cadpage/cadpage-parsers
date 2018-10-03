package net.anei.cadpage.parsers.ZSE;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenBaseParser extends FieldProgramParser {

  public ZSESwedenBaseParser(String defCity, String defState, String program) {
    super(defCity, defState, CountryCode.SE, program);
  }

  public ZSESwedenBaseParser(String defCity, String defState, String program, int flags) {
    super(defCity, defState, CountryCode.SE, program, flags);
  }
  
  private Set<String> channelSet = new HashSet<String>();
  
  @Override
  protected boolean parseFields(String[] fields, Data data) {
    channelSet.clear();
    return super.parseFields(fields, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private static final Pattern CHANNEL_PTN = Pattern.compile(".* (?:RAPS-|raps |SjvIns-|SamvFlyg-)\\d+");
  
  protected boolean isValidChannel(String field) {
    return CHANNEL_PTN.matcher(field).matches();
  }
  
  protected void addChannel(String field, Data data) {
    field = field.replace("raps ", "RAPS-");
    if (channelSet.add(field)) {
      data.strChannel = append(data.strChannel, " / ", field);
    }
  }
  
  private class MyChannelField extends ChannelField {
    public MyChannelField() {
      setPattern(CHANNEL_PTN, true);
    }
    
    @Override
    public void parse(String field, Data data) {
      addChannel(field, data);
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("La = (\\d+)(?:[^\\p{ASCII}]+| grader| ) ([\\d\\.,]+)'([NS]) +Lo = (\\d+)(?:[^\\p{ASCII}]+| grader| ) ([\\d\\.,]+)'([EW])");
  private class MyGPSField extends GPSField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      if (field.length() == 0) return true;
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      
      String gpsLoc = (match.group(3).charAt(0) == 'S' ? "-" : "+") + match.group(1) + ' ' + match.group(2) + ' ' +
                      (match.group(6).charAt(0) == 'W' ? "-" : "+") + match.group(4) + ' ' + match.group(5);
      gpsLoc = gpsLoc.replace(',', '.');
      setGPSLoc(gpsLoc, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
