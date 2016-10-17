package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COBoulderCountyBParser extends FieldProgramParser {

	public COBoulderCountyBParser() {
		super("BOULDER COUNTY", "CO", "( ADDR:ADDR APT:APT! PROB:PROB! | CALL! ADD:ADDR! BLD:APT! APT:APT! LOC:PLACE! INFO:INFO! TIME:TIME! UNITS:UNIT )");
	}
	
	@Override
	public String getProgram() {
	  return "SRC ID "+super.getProgram();
	}
	
	private static Pattern SRC_ID = Pattern.compile("([A-Z]{3,4})(\\d{6}-\\d{6}) +(.*)");
	private static Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(BLD|INFO|TIME|UNITS):");

	public boolean parseMsg(String body, Data data) {
	  Matcher mat = SRC_ID.matcher(body);
	  if (!mat.matches()) return false;
	  data.strSource = mat.group(1);
	  data.strCallId = mat.group(2);
	  body = mat.group(3);
	  body = MISSING_BLANK_PTN.matcher(body).replaceAll(" $1:");
	  
	  return super.parseMsg(body, data);
	}
	
	@Override
	  public Field getField(String name) {
	    if (name.equals("PROB")) return new MyProbField();
	    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d");
	    return super.getField(name);
	  }
	
	private static Pattern PROB = Pattern.compile("(.*) \\((L\\d)\\)(?: ([\\dA-Z]+))?");
	private class MyProbField extends CallField {
	    @Override
	    public void parse(String field, Data data) {
	      Matcher mat = PROB.matcher(field);
	      if (!mat.matches()) super.parse(field, data);
	      else {
	        data.strCall = mat.group(1).trim();
	        data.strPriority = mat.group(2);
	        data.strCode = getOptGroup(mat.group(3));
	      }
	    }
	    
	    @Override 
	    public String getFieldNames() {
	      return "CALL PRI CODE";
	    }
	  }

}
