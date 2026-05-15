import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static final String  RED	   = "\u001B[31m";
    public static final String	RESET  = "\u001B[0m";
    public static final String  BLUE   = "\u001B[36m";
    public static final String	GREEN  = "\u001B[32m";
    public static final String	YELLOW = "\u001B[33m";
    public static final String MAGENTA = "\u001B[35m";
	
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
	static String archtype;
	static String arch_type_asm;
	static String out_name;
	
	//Objects Data
	static ArrayList<String> c_objects_names = new ArrayList<String>();
	static ArrayList<String> asm_objects_names = new ArrayList<String>();
	static ArrayList<String> rust_objects_names = new ArrayList<String>();
	static ArrayList<String> ld_objects_names = new ArrayList<String>();
	
	static ArrayList<String> c_objects_paths = new ArrayList<String>();
	static ArrayList<String> asm_objects_paths = new ArrayList<String>();
	static ArrayList<String> rust_objects_paths = new ArrayList<String>();
	static ArrayList<String> ld_objects_paths = new ArrayList<String>();
   
    static File in = new File("main.forge");
    
    
    //  --Boolean--  //
    static boolean is_parts_cut = false;
    static boolean project_find = false;
    static boolean cat_find = false;
    static boolean compile_detected = false;
    static boolean link_detected = false;
    static boolean run_detected = false;
    
    static boolean project_analised = false;
    static boolean obj_analised = false;
    static boolean comp_analised = false;
    
    static boolean project_session = true;
    static boolean objects_session = true;
    static boolean compile_session = true;
    static boolean link_session = true;
    static boolean run_session = true;
    
    static boolean osdev = false;
   
    static ArrayList<String> obj_verif = new ArrayList<String>();
    
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
    						if(!comp_analised) {
    							comp_analyse();
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
    	String all_instructions = l.substring(l.indexOf("{") + 1, l.indexOf("}") - 1);
    	String[] instructions = all_instructions.split(";");
    	String value = "";
    	
    	for(String instr : instructions) {
    		String instruction = instr.strip();
    		
        	if(instruction.startsWith("arch")) {
        		arch = instruction.substring(instruction.indexOf("_") + 1, instruction.length());
        		System.out.println("Architecture : " + BLUE + arch + RESET);
        		if(arch.equals("x86_32")) {
        			archtype = "-m64";
        			arch_type_asm = "elf64";
        		} else if(arch.equals("i386")) {
        			archtype = "-m32";
        			arch_type_asm = "elf32";
        		}
        	}else if(instruction.startsWith("out")) {
        		out_name = instruction.substring(instruction.indexOf("\'") + 1, instruction.lastIndexOf("\'"));
        		System.out.println("Out name = " + BLUE + out_name + RESET);
        	} else if(instruction.startsWith("osdev")) {
        		value = instruction.substring(instruction.indexOf("=") + 2, instruction.length());
        		if(value.equals("true")) {
        			osdev = true;
        		} else if(value.equals("false")) {
        			osdev = false;
        		}
        	} else {
        		System.out.println(RED + "sForge Error : unknow element : -" + YELLOW + instruction + RED + "-" + RESET);
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
        			c_objects_names.add(name);
        			path = objet.substring(objet.indexOf("\'") + 1, objet.lastIndexOf("\'"));
        			c_objects_paths.add(path);
        		}else if(objet.startsWith("rs")) {
        			name = objet.split(" ")[1];
        			rust_objects_names.add(name);
        			path = objet.substring(objet.indexOf("\'") + 1, objet.lastIndexOf("\'"));
        			rust_objects_paths.add(path);
        		}else if(objet.startsWith("asm")) {
        			name = objet.split(" ")[1];
        			asm_objects_names.add(name);
        			path = objet.substring(objet.indexOf("\'") + 1, objet.lastIndexOf("\'"));
        			asm_objects_paths.add(path);
        		}else if(objet.startsWith("ld")) {
        			name = objet.split(" ")[1];
        			ld_objects_names.add(name);
        			path = objet.substring(objet.indexOf("\'") + 1, objet.lastIndexOf("\'"));
        			ld_objects_paths.add(path);
        		}else{
            		System.out.println(RED + "Forge Error : Object type -" + RESET + YELLOW + objet.split(" ")[0] + RESET + RED + "- unknow" + RESET);
        		}
    		}
    	}
    	
    	System.out.println();
    	System.out.print("Objects : \n");
    	System.out.print(YELLOW + "C" + RESET + " : ");
    	System.out.print(BLUE + c_objects_names + RESET + "\n");
    	System.out.print(BLUE + c_objects_paths + RESET + "\n");
    	
    	System.out.print(YELLOW + "Assembly" + RESET + " : ");
    	System.out.print(BLUE + asm_objects_names + RESET + "\n");
    	System.out.print(BLUE + asm_objects_paths + RESET + "\n");
    	
    	System.out.print(YELLOW + "Rust" + RESET + " : ");
    	System.out.print(BLUE + rust_objects_names + RESET + "\n");
    	System.out.print(BLUE + rust_objects_paths + RESET + "\n");
    	
    	System.out.print(YELLOW + "Ld" + RESET + " : ");
    	System.out.print(BLUE + ld_objects_names + RESET + "\n");
    	System.out.print(BLUE + ld_objects_paths + RESET + "\n");
    	
    	obj_analised = true;
    }
    
    static void comp_analyse() {
    	String h = compile.strip();
    	String i = h.substring(h.indexOf("{") + 1, h.lastIndexOf("}") - 1);
    	String[] all_commands = i.split(";");
    	
    	System.out.println();
    	
    	for(String g : all_commands) {
    		if(g.equals("}")){
    			break;
    		}
    		
    		String obj = "";
    		String func = "";
    		
    		try {
    			obj = g.substring(0, g.lastIndexOf("."));
    			func = g.substring(g.indexOf(".") + 1, g.lastIndexOf(")") + 1);
    		} catch (Exception e) {
    			System.out.println(RED + "sForge Java Error : " + e + RESET);
    		}
    		
    		int f = 0;

    		for(int p = 0; p < c_objects_names.size(); p++) {
    			obj = obj.strip();
    			if(obj.equals(c_objects_names.get(p))) {
    				if(func.startsWith("compile")) {
    					String out = func.substring(func.indexOf("(") + 1, func.lastIndexOf(","));
    					String std_ = func.substring(func.indexOf(",") + 1, func.lastIndexOf(")"));
    					std_ = std_.strip();
    					
    					System.out.print("\ngcc " + archtype);
    					if(osdev) {
    						System.out.print(" -ffreestanding");
    						System.out.print(" -fno-pie");
    					}
    					if(std_.equals("no_std")) {
    						System.out.print(" -nostdlib");
    					}
    					System.out.print(" -c " + c_objects_paths.get(p) + " -o ");
    					System.out.print(c_objects_names.get(p) + ".o\n");
    					
    				} else {
    					System.out.print(RED + "sForge Error : function -" + RESET);
    					System.out.print(YELLOW + func + RESET);
    					System.out.print(RED + "- is unknow ! \n" + RESET);
    				}
    				
    				f += 1;
    				obj_verif.add("true");
    			} 
    		}
    		
    		if(f == 0) {
    			obj_verif.add("false");
    		}
    		f = 0;
    		
    		for(int y = 0; y < asm_objects_names.size(); y++) {
    			obj = obj.strip();
    			if(obj.equals(asm_objects_names.get(y))) {
    				if(func.startsWith("compile")) {
    					String out = func.substring(func.indexOf("(") + 1, func.lastIndexOf(",")).strip();
    					String type = func.substring(func.indexOf(",") + 1, func.lastIndexOf(")")).strip();
    					
    					System.out.print("\nnasm -f " + arch_type_asm + " " + asm_objects_paths.get(y));
    					System.out.print(" -o " + asm_objects_names.get(y) + "." + out + "\n");
    				} else {
    					System.out.print(RED + "sForge Error : function -" + RESET);
    					System.out.print(YELLOW + func + RESET);
    					System.out.print(RED + "- is unknow ! \n");
    				}
    				
    				f += 1;
    				obj_verif.add("true");
    			}
    		}
    		
    		if(f == 0) {
    			obj_verif.add("false");
    		}
    		f = 0;
    		
    		for(int u = 0; u < rust_objects_names.size(); u++) {
    			obj = obj.strip();
    			if(obj.equals(rust_objects_names.get(u))) {
    				if(func.startsWith("compile")) {
    					String[] args = func.substring(func.indexOf("(") + 1, func.lastIndexOf(")")).split(",");
    					String out = "";
    					String std_ = "";
    					
    					int no = 0;
    					
    					for(String o : args) {
    						o = o.strip();
    						
    						if(no == 0) {
    							out = o;
    						} else if(no == 1) {
    							std_ = o;
    						}
    						
    						no += 1;
    					}
    					
    					System.out.print("\nrustc " + "--emit=obj " + "-C " + "panic=abort " + "-C " 
    					+ "opt-level=3 " + "-C " + "overflow-checks=off " + "--target " + "i686-unknown-linux-gnu " 
    							+ rust_objects_paths.get(f) + " -o " + rust_objects_names.get(f) + ".o");
    					
    				} else {
    					System.out.print(RED + "sForge Error : function -" + RESET);
    					System.out.print(YELLOW + func + RESET);
    					System.out.print(RED + "- is unknow ! \n");
    				}
    				
    				f += 1;
    				obj_verif.add("true");
    			}
    		}
    		
    		if(f == 0) {
    			obj_verif.add("false");
    		}
    		f = 0;
    		
    	}
    	
    	comp_analised = true;
    }
}
