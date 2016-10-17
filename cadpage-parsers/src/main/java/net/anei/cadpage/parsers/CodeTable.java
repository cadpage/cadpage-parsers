package net.anei.cadpage.parsers;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Lookup table designed to identify call descriptions associated with particular call codes.  
 * Constructor is passed a table of codes and descriptions.  When searching the table for a
 * code match, an exact match is not required, the table will return the description associated
 * with the longest key code that is a prefix of the passed call code. 
 */
public class CodeTable {
  
  public static class Result {
    private String code;
    private String description;
    private String remainder;
    
    private Result(Map.Entry<String,String> entry, String text) {
      this.code = entry.getKey();
      this.description = entry.getValue();
      this.remainder = text.substring(code.length()).trim();
    }
    
    void setCode(String code) {
      this.code = code;
    }
    
    public String getCode() {
      return code;
    }
    
    public void setDescription(String description) {
      this.description = description;
    }
    
    public String getDescription() {
      return description;
    }
    
    public void setRemainder(String remainder) {
      this.remainder = remainder;
    }
    
    public String getRemainder() {
      return remainder;
    }
  }
  
  private TreeMap<String,String> codeMap = new TreeMap<String,String>(new Comparator<String>(){
    @Override
    public int compare(String str1, String str2) {
      return -str1.compareTo(str2);
    }});
  
  private int minCodeLen = Integer.MAX_VALUE;
  
  public CodeTable(String ... table) {
    if (table.length % 2 != 0) {
      throw new RuntimeException("CodeTable constructor must have even number of of entries");
    }
    for (int ndx = 0; ndx < table.length; ndx += 2) {
      put(table[ndx], table[ndx+1]);
    }
  }
  
  public void put(String key, String value) {
    if (key.length() < minCodeLen) minCodeLen = key.length();
    codeMap.put(key, value);
  }

  /**
   * Look for a call description corresponding to a specific code
   * @param code call code
   * @return description associated with code or null if none found
   */
  public String getCodeDescription(String code) {
    return getCodeDescription(code, false);
  }

  /**
   * Look for a call description corresponding to a specific code
   * @param code call code
   * @return description associated with code or null if none found
   */
  public String getCodeDescription(String code, boolean reqSpace) {
    Result res = getResult(code, reqSpace);
    if (res == null) return null;
    return res.getDescription();
  }

  /**
   * Look for a call description corresponding to a specific code
   * @param code call code
   * @param reqSpace true if successful match requires a blank terminator
   * @return result object describing result if found, null otherwise
   */
  public Result getResult(String code) {
    return getResult(code, false);
  }

  /**
   * Look for a call description corresponding to a specific code
   * @param code call code
   * @return result object describing result if found, null otherwise
   */
  public Result getResult(String code, boolean reqSpace) {
    
    // Search the code dictionary sorted map for the highest entry less than or
    // equal to call code.  If the code starts with this string, we have a
    // match.  If not, we have to keep searching backward through the sorted map
    // for the entry less than or equal to the code
    
    // We reversed the tree order so we can accomplish this trick without
    // needing a backward read feature, with Android seems to be lacking
    if (code.length() < minCodeLen) return null;
    String  minCode = code.substring(0, minCodeLen);
    SortedMap<String,String> tail =  codeMap.tailMap(code);
    for (Map.Entry<String,String> entry : tail.entrySet()) {
      String key = entry.getKey();
      if (code.startsWith(key)) {
        if (!reqSpace) return new Result(entry, code);;
        int len = key.length();
        if (len == code.length() || code.charAt(len) == ' ') return new Result(entry, code);
      }
      if (!code.startsWith(minCode)) break;
    }
    return null;
  }
  
  /**
   * Return description corresponding to matching code.
   * Normally we would use a HashSet if all were were interested in was
   * exact code matches, but there is at least one case where a parser
   * needs to do both partial and exact code lookups, in which case they
   * can use this method for the exact code lookup
   * @param code code to search for
   * @return matching description if found, or null if not
   */
  public String getMatch(String code) {
    return codeMap.get(code);
  }
}
