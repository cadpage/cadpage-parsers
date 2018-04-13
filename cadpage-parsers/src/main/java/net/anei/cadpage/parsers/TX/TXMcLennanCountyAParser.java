package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class TXMcLennanCountyAParser extends DispatchProQAParser {

  public TXMcLennanCountyAParser() {
    super("MCLENNAN COUNTY", "TX", 
          "PRI ID! TIME CALL+? UNKNOWN ADDR CITY INFO+");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("(P\\d) .*");
    if (name.equals("UNKNOWN")) return new SkipField("<Unknown>", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static Pattern X_IS = Pattern.compile("xst is (.*) --  (.*)");
  private static Pattern X_DELIM = Pattern.compile("(?:XST:|X-) ?(.*?)(?:\\.{2}(.*))?", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {
      //parse xstreet if present
      Matcher mat = X_IS.matcher(field);
      if (mat.matches()) {
        data.strCross = append(data.strCross, " / ", mat.group(1).trim());
        field = mat.group(2).trim();
      } else {
        mat = X_DELIM.matcher(field);
        if (mat.matches()) {
          data.strCross = append(data.strCross, " / ", mat.group(1).trim());
          field = getOptGroup(mat.group(2));
        }
      }
      
      //append possibly modified field to strSupp
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO X";
    }
  }

}
