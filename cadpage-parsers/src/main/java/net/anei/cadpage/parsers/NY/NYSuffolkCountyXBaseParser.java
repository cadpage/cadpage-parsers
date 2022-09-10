package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Base class for new Suffolk County parser based on ** field separators
 */
public class NYSuffolkCountyXBaseParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("\\*\\* |\n+ *\\*\\*|\n");
  
  public NYSuffolkCountyXBaseParser(String defCity, String defState, String program) {
    this(null, defCity, defState, program);
  }
  
    public NYSuffolkCountyXBaseParser(String[] cityList, String defCity, String defState, String program) {
      super(cityList, defCity, defState, program);
    }

	  @Override
	  protected boolean parseMsg(String body, Data data) {
	    if (!body.startsWith("** ")) return false;
	    body = body.substring(3).trim();
	    if (body.endsWith("**")) body = body.substring(0, body.length()-2).trim();
	    return parseFields(DELIM.split(body), data);
	  }

    @Override
    protected Field getField(String name) {
      if (name.equals("CODE")) return new CodeField("\\d{1,2}-?[A-Z]-?\\d{1,2}[A-Za-z]?", true);
      if (name.equals("ID")) return new BaseIdField(false, false);
      if (name.equals("IDP")) return new BaseIdField(true, false);
      if (name.equals("ID_INFO")) return new BaseIdField(false, true);
      if (name.equals("TOA")) return new TOAField(false);
      if (name.equals("TOAP")) return new TOAField(true);
      if (name.equals("INFO")) return new MyInfoField();
      return super.getField(name);
    }
    
    private static final Pattern ID_PTN = Pattern.compile("(\\d{4}-\\d{6})\\b *");
    private class BaseIdField extends IdField {
      
      private boolean allowPartial;
      private boolean trailInfo;
      
      public BaseIdField(boolean allowPartial, boolean trailInfo) {
        this.allowPartial = allowPartial;
        this.trailInfo = trailInfo;
      }
      
      @Override
      public boolean canFail() {
        return true;
      }
      
      @Override
      public boolean checkParse(String field, Data data) {
        Matcher match = ID_PTN.matcher(field);
        if (trailInfo) {
          if (match.lookingAt()) {
            super.parse(match.group(1), data);
            data.strSupp = field.substring(match.end());
            return true;
          }
        }
        
        if (match.matches()) {
          super.parse(field, data);
          return true;
        }
        
        // OK, this isn't valid, but we check to see if we allow
        // truncated TOA fields
        if (!allowPartial || field.length() == 0) return false;
        if (getRelativeField(+1).length() > 0) return false;
        return "NNNN-NNNNNN".startsWith(field.replaceAll("\\d", "N"));
      }
      
      @Override
      public void parse(String field, Data data) {
        if (!checkParse(field, data)) abort();
      }
      
      @Override
      public String getFieldNames() {
        return trailInfo ? "ID INFO?" : "ID";
      }
    }
	  
	  private static final Pattern TOA_PTN = Pattern.compile("\\d\\d:\\d\\d \\d\\d-\\d\\d-\\d\\d");
	  private class TOAField extends TimeDateField {
	    
	    private boolean allowPartial;
	    
	    public TOAField(boolean allowPartial) {
	      this.allowPartial = allowPartial;
	    }
	    
	    @Override
	    public boolean checkParse(String field, Data data) {
	      Matcher match = TOA_PTN.matcher(field);
	      if (match.matches()) {
	        super.parse(field.replace('-', '/'), data);
	        return true;
	      }
	      
	      // OK, this isn't valid, but we check to see if we allow
	      // truncated TOA fields
	      if (!allowPartial || field.length() == 0) return false;
	      if (getRelativeField(+1).length() > 0) return false;
	      if (!"NN:NN NN-NN-NN".startsWith(field.replaceAll("\\d", "N"))) return false;
	      if (field.length() >= 5) data.strTime = field.substring(0,5);
	      return true;
	    }
	    
	    @Override
	    public void parse(String field, Data data) {
	      if (!checkParse(field, data)) abort();
	    }
	  }
	  
	  private class MyInfoField extends InfoField {
	    @Override
	    public void parse(String field, Data data) {
	      if (field.equals(data.strCode)) return;
	      super.parse(field, data);
	    }
	  }
	  
	}
	