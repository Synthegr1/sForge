import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static final String  RED	   = "\u001B[31m";
    public static final String	RESET  = "\u001B[0m";
    public static final String  BLUE   = "\u001B[36m";
    public static final String	GREEN  = "\u001B[32m";
    public static final String	YELLOW = "\u001B[33m";
	
    static String project = "";
    static String objects = "";
    static String compile = "";
    static String link = "";
    static String run = "";
    
    static String project_name;
	static String cat_name;
	
	//  --Collected Data--   //
	// Project Data
	static String arch;
	static String out_name;
	
	//Objects Data
	static String[] c_objects_name;
	static String[] asm_objects_name;
	static String[] rust_objects_name;
	static String[] ld_objects_name;
	
	static String[] c_objects_path;
	static String[] asm_objects_path;
	static String[] rust_objects_path;
	static String[] ld_objects_path;
   
    static File in = new File("main.txt");
    
    static boolean is_parts_cut = false;
    static boolean project_find = false;
    static boolean cat_find = false;
    static boolean compile_detected = false;
    static boolean link_detected = false;
    static boolean run_detected = false;
    
    static boolean project_analised = false;
    static boolean obj_analised = false;
    
    static boolean project_session = true;
    static boolean objects_session = true;
    static boolean compile_session = true;
    static boolean link_session = true;
    static boolean run_session = true;
   
	public static void main(String[] args) {
		int curs = 0;
		boolean is_branch_def = false;
		
    	try (Scanner reader = new Scanner(in)) {
    		while(reader.hasNextLine()) {
    			curs = curs + 1;
    			String data = reader.nextLine();
    			
    			//System.out.println(data);
    			
    			if(is_a_comment(data)) {
    				continue;
    			} else {
    				if(is_branch_def) {
    					if(!is_parts_cut) {
    						def_parts();
    					} else if(is_parts_cut) {
    						if(!project_analised) {
    							project_analyse();
    						}
    						if(!obj_analised) {
    							obj_analyse();
    						}
    					}
    				} else {
    					def_branch();
    					is_branch_def = true;
    				}
    			}
    		} 
    	}catch (FileNotFoundException e) {
			System.out.println(RED + "File Not Found !" + RESET);
		}
    	
    	int y = 0;
    	System.out.println();
    	
    	if(compile_session) {
    		System.out.println(GREEN + "Compile section detected !" + RESET);
    		y += 1;
    	}
    	if(link_session) {
    		System.out.println(GREEN + "Link section detected !" + RESET);
    		y += 1;
    	}
    	if(run_session) {
    		System.out.println(GREEN + "Run section detected !" + RESET);
    		y += 1;
    	}
    	if(objects_session) {
    		System.out.println(GREEN + "Object section detected !" + RESET);
    		y += 1;
    	}
    	if(project_session) {
    		System.out.println(GREEN + "Project section detected !" + RESET);
    		y += 1;
    	}
    	
    	if(y == 5) {
    		System.out.println();
    		System.out.println(GREEN + "All section are find, complete success !" + RESET);
    	} else if(y < 5) {
    		System.out.println();
    		System.out.println(RED + "Less one section !" + RESET);
    	} else if(y > 5) {
    		System.out.println();
    		System.out.println(RED + "Add one section !" + RESET);
    	} else {
    		System.out.println();
    		System.out.println(RED + "Error !" + RESET);
    	}
    }
    
    static boolean is_a_comment(String input) {
    	String test = input.stripLeading();
    	if(test.startsWith("//")) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    static void def_branch() {
    	boolean is_find = false;
    	
    	try(Scanner r = new Scanner(in)){
    		while(r.hasNextLine() && !is_find) {
    			String f = r.nextLine();
    			
        		String data = f.strip();
            	if(!is_a_comment(data)) {
            		if(data.startsWith("branch")) {
            			int start = data.indexOf(" ") + 1;
            			int end = data.lastIndexOf("{");
            			String branch_name = data.substring(start, end);
            			System.out.println("Branch name : " + BLUE + branch_name + RESET);
            			is_find = true;
            		} else {
            			
            		}
            	}
    		}
    	} catch (FileNotFoundException e) {
    		System.out.println("Error");
    	}
    }
    
    static void def_parts() {
    	is_parts_cut = true;
    	
    	boolean is_find = false;
    	
    	try(Scanner r = new Scanner(in)){
    		while(r.hasNextLine() && !is_find) {
    			String f = r.nextLine();
        		String data = f.strip();
    			
    			if(!is_a_comment(data)) {
    				if(data.startsWith("project") && !project_find) {
    					project_name = data.substring(data.indexOf(" ") + 1, data.lastIndexOf("{"));
    					System.out.println("Project name : " + BLUE + project_name + RESET);
    					project_find = true; 
    					project_session = false;
    				} else if(data.startsWith("cat")) {
    					cat_name = data.substring(data.indexOf(" ") + 1, data.lastIndexOf("{"));
    					cat_find = true;
    					objects_session = false;
    				} else if(data.startsWith("forge")) {
    					String o = data.substring(data.indexOf(" ") + 1, data.lastIndexOf("{"));
    					String type = o.strip();
    					
    					if(!compile_detected && type.equals("compile")) {
    						compile_detected = true;
    						compile_session = false;
    					} else if(!link_detected && type.equals("link")) {
    						link_detected = true;
    						link_session = false;
    					} else if(!run_detected && type.equals("run")) {
    						run_detected = true;
    						run_session = false;
    					} else {
    						System.out.println(RED + "Forge error : Unknown -Forge- section detected" + RESET);
    					}
    				}
    			
    				//Project Data
    				if(!project_session) {
						String t = data.strip();
						if(t.equals("}")) {
							project_session = true;
							project = project + data + "\n";
						} else {
							project = project + data + "\n";
						}
					}
    				
    				//Objects
    				if(!objects_session) {
						String t = data.strip();
						if(t.equals("}")) {
							objects_session = true;
							objects = objects + data + "\n";
						} else {
							objects = objects + data + "\n";
						}
					}
    				
    				//Compilation Data
    				if(!compile_session) {
						String t = data.strip();
						if(t.equals("}")) {
							compile_session = true;
							compile = compile + data + "\n";
						} else {
							compile = compile + data + "\n";
						}
					}
    				
    				//Link Data
    				if(!link_session) {
						String t = data.strip();
						if(t.equals("}")) {
							link_session = true;
							link = link + data + "\n";
						} else {
							link = link + data + "\n";
						}
					}
    				
    				//Qemu logs
    				if(!run_session) {
						String t = data.strip();
						if(t.equals("}")) {
							run_session = true;
							run = run + data + "\n";
						} else {
							run = run + data + "\n";
						}
					}
    				
    				
    				
    			}
    		}
    	}catch(FileNotFoundException e) {
    		System.out.println(RED + "File not found error !" + RESET);
    	}
    	
    }
    
    static void project_analyse() {
    	String l = project.strip();
    	String all_instructions = l.substring(l.indexOf("{") + 1, l.indexOf("}"));
    	String[] instructions = all_instructions.split(";");
    	
    	for(String instr : instructions) {
    		String instruction = instr.strip();
    		
        	if(instruction.startsWith("arch")) {
        		arch = instruction.substring(instruction.indexOf("_") + 1, instruction.length());
        		System.out.println("Architecture : " + BLUE + arch + RESET);
        	}else if(instruction.startsWith("out")) {
        		out_name = instruction.substring(instruction.indexOf("\'") + 1, instruction.lastIndexOf("\'"));
        		System.out.println("Out name = " + BLUE + out_name + RESET);
        	}
    	}
    	
    	project_analised = true;
    }
    
    static void obj_analyse() {
    	String t = objects.strip();
    	String all_objects = t.substring(t.indexOf("{") + 1, t.indexOf("}"));
    	String[] objects_defs = all_objects.split(";");
    	
    	for(String ob : objects_defs) {
    		String objet = ob.strip();
    		String name = "";
    		String path = "";
    		
    		if(objet.isEmpty() || objet.equals("}")) continue;
    		
    		if(!is_a_comment(objet)) {
        		if(objet.startsWith("c")) {
        			name = objet.split(" ")[1];
        			System.out.println(name);
        			path = objet.substring(objet.indexOf("\'"), objet.lastIndexOf("\'"));
        			
        		}else if(objet.startsWith("rs")) {
        			name = objet.split(" ")[1];
        			System.out.println(name);
        			path = objet.substring(objet.indexOf("\'"), objet.lastIndexOf("\'"));
        			
        		}else if(objet.startsWith("asm")) {
        			name = objet.split(" ")[1];
        			System.out.println(name);
        			path = objet.substring(objet.indexOf("\'"), objet.lastIndexOf("\'"));
        			
        		}else if(objet.startsWith("ld")) {
        			name = objet.split(" ")[1];
        			System.out.println(name);
        			path = objet.substring(objet.indexOf("\'"), objet.lastIndexOf("\'"));
        			
        		}else{
            		System.out.println(RED + "Forge Error : Object type -" + RESET + YELLOW + objet.split(" ")[0] + RESET + RED + "- unknow" + RESET);
        		}
    		}
    	}
    	
    	obj_analised = true;
    }
}
