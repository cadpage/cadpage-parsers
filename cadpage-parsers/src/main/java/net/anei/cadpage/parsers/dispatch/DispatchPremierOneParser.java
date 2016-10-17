package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchPremierOneParser extends FieldProgramParser {
  
  private HtmlDecoder decoder;

  protected DispatchPremierOneParser(String defCity, String defState) {
    super(defCity, defState, 
          "Status:STATUS! Inc_#:ID Inc_Type:CODE! Inc_Desc:CALL! Location:ADDR! Cross_Strs:X END");
    decoder = new HtmlDecoder();
  }
  
  @Override
  public String getFilter() {
    return "RCCC@co.rock.wi.us";
  }
  
  private static final Pattern TIME_DATE_PTN = Pattern.compile("(\\d{1,2}:\\d{2}:\\d{2} [AP]M) +(\\d{1,2}/\\d{1,2}/\\d{4})(?:\\n.*)?", Pattern.DOTALL);
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    String[] flds = decoder.parseHtml(body);
    if (flds == null) return false;
    
    int state = 0;
    for (String field : flds) {
      switch (state) {
      case 0:
        if (field.equals("PremierOne Notification")) state = 1;
        break;
        
      case 1:
        if (!super.parseHtmlMsg(subject, field, data)) return false;
        state = 2;
        break;
        
      case 2:
        Matcher match = TIME_DATE_PTN.matcher(field);
        if (match.matches()) {
          setTime(TIME_FMT, match.group(1), data);
          data.strDate = match.group(2);
          return true;
        }
        break;
      }
    }
    
    return false;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " TIME DATE";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("STATUS")) return new SkipField("Open", true);
    if (name.equals("X")) return new BaseCrossField();
    return super.getField(name);
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

}
