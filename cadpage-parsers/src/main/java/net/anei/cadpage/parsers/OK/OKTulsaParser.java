package net.anei.cadpage.parsers.OK;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.general.GeneralParser;


public class OKTulsaParser extends FieldProgramParser {
  
  private GeneralParser generalParser;
  
  public OKTulsaParser() {
    super("TULSA", "OK",
          "( ADDR/Z PLACE CALL MAP! PLACE ID " +
          "| CALL ADDR PLACE ID MAP! ) END");
    
    // We need to invoke methods from the GeneralParser and FieldProgramParser
    // in the same parser class.  Which can not be done.  We get around this
    // by building are own private GeneralParser class to handle the general
    // processing
    generalParser = new GeneralParser("TULSA", "OK"){
      @Override
      protected boolean isPageMsg(String subject, String body) {
        return true;
      }
      
    };
  }
  
  @Override
  public String getFilter() {
    return "pagealert@cityoftulsa.org,@berryhillfire.com";
  }
  
  private static final Pattern MARKER = Pattern.compile("^(?:PAGE-OUT / )?(\\d\\d:\\d\\d[AP]M) [A-Z][A-Za-z]{2} +(\\d\\d/\\d\\d/\\d\\d(?:\\d\\d)?) ");
  private static final Pattern DELIM_PTN = Pattern.compile("\\.\\.+|//+");
  
  private static final SimpleDateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");
  
  private static final Pattern DELIM = Pattern.compile(" [.,]");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // There are two very different formats being processed
    // Start with the older one first
    
    // See if message has the right start signature
    Matcher match = MARKER.matcher(body);
    if (match.find()) {
      setFieldList("DATE TIME CALL ADDR X PHONE NAME ID INFO PLACE");
      
      setTime(TIME_FMT, match.group(1), data);
      data.strDate = match.group(2);
      body = body.substring(match.end()).trim();
      
      // There are some constructs we know are delimiters that the general parser doesn't
      body = DELIM_PTN.matcher(body).replaceAll(";");
      
      // That's all we can do, time for the general parser to do what it can
      if (generalParser.parseMsg(subject, body, data)) return true;
      
      // If it fails, it can only be because it couldn't find an address
      // We know this is a CAD page so we don't want to fail, so we'll just
      // do our own general alert
      String saveDate = data.strDate;
      String saveTime = data.strTime;
      data.parseGeneralAlert(this, body);
      data.strDate = saveDate;
      data.strTime = saveTime;
      return true;
    }

    // Otherwise, see if we have a nice neat delimited page format
    return parseFields(DELIM.split(body, -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[A-Z]{3,4}\\d{4}-?\\d+", true);
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NOT FOUND")) return;
      field = stripFieldStart(field, "MP ");
      super.parse(field, data);
    }
  }

}
