package net.anei.cadpage.parsers;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Lookup table that can perform incomplete text match searches.  When called to search the
 * table, it will return the longest entry that is a prefix of the search string. 
 */
public class CodeSet {
  
  public static final String DELIMS = " ,;/";
  
  private TreeSet<String> codeSet = new TreeSet<String>(new Comparator<String>(){
    @Override
    public int compare(String str1, String str2) {
      return -str1.compareTo(str2);
    }
  });
  
  // Minimum entry length
  private int minCodeLen = Integer.MAX_VALUE;
  
  public CodeSet(String ... table) {
    for (String code : table) add(code);
  }
  
  public CodeSet(Properties codeTable) {
    @SuppressWarnings("unchecked")
    Enumeration<String> e = (Enumeration<String>) codeTable.propertyNames();
    while (e.hasMoreElements()) {
      add(e.nextElement());
    }
  }
  
  public void add(String code) {
    if (code.length() < minCodeLen) minCodeLen = code.length();
    codeSet.add(code);
  }


  /**
   * Look for a code that is a prefix to search string
   * @param code search string
   * @return longest table entry that is a prefix of search string or
   *          null if there is no such entry
   */
  public String getCode(String code) {
    return getCode(code, false);
  }

  /**
   * Look for a code that is a prefix to search string
   * @param code search string
   * @param reqSpace true if successful match requires a blank terminator
   * @return longest table entry that is a prefix of search string or
   *          null if there is no such entry
   */
  public String getCode(String code, boolean reqSpace) {
    
    // Search the code dictionary sorted map for the highest entry less than or
    // equal to call code.  If the code starts with this string, we have a
    // match.  If not, we have to keep searching backward through the sorted map
    // for the entry less than or equal to the code
    
    // We reversed the tree order so we can accomplish this trick without
    // needing a backward read feature, with Android seems to be lacking
    
    if (code.length() < minCodeLen) return null;
    String  minCode = code.substring(0, minCodeLen);
    SortedSet<String> tail =  codeSet.tailSet(code);
    for (String key : tail) {
      if (code.startsWith(key)) {
        if (!reqSpace) return key;
        int len = key.length();
        if (len == code.length() || DELIMS.indexOf(code.charAt(len))>=0) return key;
      }
      if (!code.startsWith(minCode)) break;
    }
    return null;
  }
}
