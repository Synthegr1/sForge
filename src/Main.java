import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.ProcessBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileReader;
import java.io.BufferedReader;

public class Main {
	static double version;
	
    public static final String  RED	   = "\u001B[31m";
    public static final String	RESET  = "\u001B[0m";
    public static final String  BLUE   = "\u001B[36m";
    public static final String	GREEN  = "\u001B[32m";
    public static final String	YELLOW = "\u001B[33m";
    public static final String MAGENTA = "\u001B[35m";
    
    static int delay = 50;
    
    static String main_out = "";
    static String path;
	
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
	static String arch_type_ld;
	static String out_name;
	static String project_path;
	static String branchname;
	
	//Objects Data
	static ArrayList<String> c_objects_names = new ArrayList<String>();
	static ArrayList<String> asm_objects_names = new ArrayList<String>();
	static ArrayList<String> rust_objects_names = new ArrayList<String>();
	static ArrayList<String> ld_objects_names = new ArrayList<String>();
	
	static ArrayList<String> c_objects_paths = new ArrayList<String>();
	static ArrayList<String> asm_objects_paths = new ArrayList<String>();
	static ArrayList<String> rust_objects_paths = new ArrayList<String>();
	static ArrayList<String> ld_objects_paths = new ArrayList<String>();
   
	static ArrayList<String> c_objects_names_o = new ArrayList<String>();
	static ArrayList<String> asm_objects_names_o = new ArrayList<String>();
	static ArrayList<String> rust_objects_names_o = new ArrayList<String>();
	
    static File in;
    
    
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
    static boolean link_analised = false;
    static boolean run_analised = false;
    static boolean env_set = false;
    
    static boolean project_session = true;
    static boolean objects_session = true;
    static boolean compile_session = true;
    static boolean link_session = true;
    static boolean run_session = true;
    
    static boolean osdev = false;
    static boolean infos = false;
    static boolean print = true;
   
    static ArrayList<String> obj_verif = new ArrayList<String>();
    
    
    // Arraylist Commandes process builder
    static ArrayList<String> c_command = new ArrayList<String>();
    static ArrayList<String> rs_command = new ArrayList<String>();
    static ArrayList<String> nasm_command = new ArrayList<String>();
    static ArrayList<String> ld_command = new ArrayList<String>();
    static ArrayList<String> obj_command = new ArrayList<String>();
    static ArrayList<String> qemu_command = new ArrayList<String>();
    
    static ArrayList<String> update_command = new ArrayList<String>();
    static ArrayList<String> git_command = new ArrayList<String>();
    
    static ArrayList<String> all_args = new ArrayList<String>();
    
	public static void main(String[] args) {
		
		all_args.add("build");
		all_args.add("-i");
		all_args.add("help");
		all_args.add("update");
		all_args.add("--version");
		
		
		File t = new File("/home/gabrielrebillat/Downloads/sForge-main/temp/version");
		t.delete();
		
		
		git_command.add("wget");
		git_command.add("-P");
		git_command.add("/home/gabrielrebillat/Downloads/sForge-main/temp");
		git_command.add("https://raw.githubusercontent.com/Synthegr1/sForge/refs/heads/main/src/version");
		
		ProcessBuilder ver_sub = new ProcessBuilder(git_command);
		
		try{
			Process ver_proc = ver_sub.start();
			
			try {
				int exitcode = ver_proc.waitFor();
				if(exitcode != 0) {
					System.out.println("sForge WGET - Update Error : " + exitcode);
				}
			} catch (Exception e) {
				System.out.println("sForge Java Error : .waitFor --> " + BLUE + e);
			}
			
			try {
				TimeUnit.MILLISECONDS.sleep(delay);
			} catch (Exception e) {
				System.out.println(RED + "Java Error TimeUnit" + e + RESET);
			}
			
			
			try (BufferedReader br = new BufferedReader(new FileReader("/home/gabrielrebillat/Downloads/sForge-main/temp/version"))) {
	            String line =  null;
	            while ((line = br.readLine()) != null) {
	                //System.out.println(line);
	            	line = line.strip();
		            if(line != null) {
		            	double version_t = Double.parseDouble(line);
			            version = version_t;
		            }
	            	continue;
	            }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}catch(Exception e) {
			
		}
        
		
		if(args[0].equals("help")) {
			
			System.out.println("sForge 🪐 v" + version + " -- Help :");
			System.out.println("	sforge + args");
			System.out.println("		--> " + YELLOW + "help " + RESET + ": help page");
			System.out.println("		--> " + YELLOW + "build" + RESET);
			System.out.println("			--> " + YELLOW + "-i " + RESET + ": don't print all sForge Compilations informations");
			System.out.println("				--> " + BLUE + "filename.forge" + RESET);
			System.out.println("			--> " + BLUE + "filename.forge" + RESET);
			System.out.println("		--> " + YELLOW + "update" + RESET + ": update sForge");
			System.out.println("			--> " + YELLOW + "-m " + RESET + ": massive update from github");
			System.out.println("		--> " + YELLOW + "--version" + RESET + " : version info");
			
		} else if(args[0].equals("update")) {
			update_command.add("python3");
			update_command.add("/home/gabrielrebillat/Downloads/sForge-main/update.py");
			
			ProcessBuilder up_sub = new ProcessBuilder(update_command);
			up_sub.inheritIO();
			try {
				
				Process update_proc = up_sub.start(); 	
				
				try {
					int exitcode = update_proc.waitFor();
					if(exitcode != 0) {
						System.out.println("sForge PYTHON - UPDATE Error : " + exitcode);
					}
				} catch (Exception e) {
					System.out.println("sForge Java Error : .waitFor --> " + BLUE + e);
				}
				
				try {
					TimeUnit.MILLISECONDS.sleep(delay);
				} catch (Exception e) {
					System.out.println(RED + "Java Error TimeUnit" + e + RESET);
				}
				
				
			} catch(IOException ioe) {
				ioe.printStackTrace();
				
			}
			
			
			System.out.println("Update infos :");
		}
		else if(args[0].equals("--version")) {
			System.out.println("sForge 🪐 version : " + YELLOW + version);
		}
		else if(args[0].equals("build")){
			
			for(int i = 1; i < args.length; i++) {
				if(args[i].startsWith("-")) {
					String letter = args[i].substring(args[i].indexOf("-") + 1, args[i].length());
					
					if(letter.equals("i")) {
						print = false;
					}
				} else {
					try {
						in = new File(args[i]);
					} catch(Exception e) {
						System.out.println(RED + "sForge Error : need to specify the .forge file path !");
						System.exit(0);
					}
				}
			}
			
			pr();
			
		} else if(args[0].equals("clean")) {
			System.out.println("sForge 🪐 v" + version + " -- CLEAN");
			print = false;
			
			for(int i = 1; i < args.length; i++) {
				if(args[i].startsWith("-")) {
					String letter = args[i].substring(args[i].indexOf("-") + 1, args[i].length());
					
					
					
				} else {
					try {
						in = new File(args[i]);
					} catch(Exception e) {
						System.out.println(RED + "sForge Error : need to specify the .forge file path !");
						System.exit(0);
					}
				}
			}
			
			clean();
			
		}
		else if(args[0].startsWith("test")) {
			
		}
		else {
			System.out.print(RED + "sForge error : Unknown argument --> " + YELLOW);
			for(int i = 0; i < args.length; i++) {
				for(int g = 0; g < all_args.size(); g++) {
					if(!args[i].equals(all_args.get(g))) {
						System.out.print(args[i]);
						System.out.print("\n");
						System.exit(0);
					}
				}
			}
		}
		
		t.delete();
    }
	
	
	// ---- FUNCTIONS ---- //
	
	
	
	static void clean() {
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
    							System.out.println(arch);
    						}
    						if(!obj_analised) obj_analyse();
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
			System.out.println(RED + "File Not Found --> " + RESET + YELLOW + in);
			System.exit(0);
		}
    	
    	String u = project_path + "/" + project_name + "-" + branchname + "-sForge/env/lingot/";
    	System.out.println(RED + "Removing : ");
    	System.out.println(u);
    	
    	for(int y = 0; y < asm_objects_names_o.size(); y++) {
    		File in = new File(u + asm_objects_names_o.get(y));
    		System.out.println(u + asm_objects_names_o.get(y));
    		in.delete();
    		
    		try {
    			TimeUnit.MILLISECONDS.sleep(500);
    		} catch (Exception e) {
    			System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    		}
    		
    	}
    	
    	for(int y = 0; y < c_objects_names_o.size(); y++) {
    		File in = new File(u + c_objects_names_o.get(y));
    		System.out.println(u + c_objects_names_o.get(y));
    		
    			in.delete();
    		try {
    			TimeUnit.MILLISECONDS.sleep(500);
    		} catch (Exception e) {
    			System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    		}
    		
    	}
    	
    	for(int y = 0; y < rust_objects_names_o.size(); y++) {
    		File in = new File(u + rust_objects_names_o.get(y));
    		System.out.println(u + rust_objects_names_o.get(y));
    		in.delete();
    		
    		try {
    			TimeUnit.MILLISECONDS.sleep(500);
    		} catch (Exception e) {
    			System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    		}
    		
    	}
    	for(int y = 0; y < ld_objects_names.size(); y++) {
    		File in = new File(u + ld_objects_names.get(y) + ".ld");
    		System.out.println(u + ld_objects_names.get(y) + ".ld" + RESET);
    		in.delete();
    		
    		try {
    			TimeUnit.MILLISECONDS.sleep(500);
    		} catch (Exception e) {
    			System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    		}
    		
    	}
	}
	
	
	// ------- //
	
	static void pr() {
		System.out.println("sForge v" + version + " -- BUILD ");
		if(print)System.out.println("");
		
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
    						if(osdev) {
	    						if(!env_set) {
	    							env_setup();
	    						}
	    						if(!obj_analised) {
	    							obj_analyse();
	    						}
	    						if(!comp_analised) {
	    							comp_analyse();
	    						}
	    						if(!link_analised) {
	    							link_analyse();
	    						}
	    						if(!run_analised) {
	    							run_analyse();
	    						}
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
    	if(print)System.out.println();
    	
    	if(compile_session) {
    		if(print)System.out.println(GREEN + "Compile section detected !" + RESET);
    		y += 1;
    	}
    	if(link_session) {
    		if(print)System.out.println(GREEN + "Link section detected !" + RESET);
    		y += 1;
    	}
    	if(run_session) {
    		if(print)System.out.println(GREEN + "Run section detected !" + RESET);
    		y += 1;
    	}
    	if(objects_session) {
    		if(print)System.out.println(GREEN + "Object section detected !" + RESET);
    		y += 1;
    	}
    	if(project_session) {
    		if(print)System.out.println(GREEN + "Project section detected !" + RESET);
    		y += 1;
    	}
    	
    	if(y == 5) {
    		if(print)System.out.println();
    		if(print)System.out.println(GREEN + "All section are find, complete success !" + RESET);
    	} else if(y < 5) {
    		if(print)System.out.println();
    		if(print)System.out.println(RED + "Less one section !" + RESET);
    	} else if(y > 5) {
    		if(print)System.out.println();
    		if(print)System.out.println(RED + "Add one section !" + RESET);
    	} else {
    		if(print)System.out.println();
    		if(print)System.out.println(RED + "Error !" + RESET);
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
            			branchname = branch_name;
            			if(print)System.out.println("Branch name : " + BLUE + branch_name + RESET);
            			is_find = true;
            		} else {
            			
            		}
            	}
    		}
    	} catch (FileNotFoundException e) {
    		if(print)System.out.println("Error");
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
    					project_name = project_name.strip();
    					if(print)System.out.println("Project name : " + BLUE + project_name + RESET);
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
        		if(print)System.out.println("Architecture : " + BLUE + arch + RESET);
        		if(arch.equals("x86_32")) {
        			archtype = "-m64";
        			arch_type_asm = "elf64";
        		} else if(arch.equals("i386")) {
        			archtype = "-m32";
        			arch_type_asm = "elf32";
        			arch_type_ld = "elf_i386";
        		}
        	}else if(instruction.startsWith("out")) {
        		out_name = instruction.substring(instruction.indexOf("\'") + 1, instruction.lastIndexOf("\'"));
        		if(print)System.out.println("Out name = " + BLUE + out_name + RESET);
        	} else if(instruction.startsWith("osdev")) {
        		value = instruction.substring(instruction.indexOf("=") + 2, instruction.length());
        		if(value.equals("true")) {
        			osdev = true;
        		} else if(value.equals("false")) {
        			osdev = false;
        		}
        	}else if(instruction.startsWith("project_path")) {
        		project_path = instruction.substring(instruction.indexOf("\'") + 1, instruction.lastIndexOf("'"));
        		if(print)System.out.println("Project location : " + BLUE + project_path + RESET);
        	}else if(instruction.startsWith("infos")){
        		value = instruction.substring(instruction.indexOf("=") + 2, instruction.length());
        		if(value.equals("true")) {
        			infos = true;
        		} else if(value.equals("false")) {
        			infos = false;
        		}
        	}else {
        		System.out.println(RED + "sForge Error : unknow element : -" + YELLOW + instruction + RED + "-" + RESET);
        		System.exit(0);
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
            		System.exit(0);
        		}
    		}
    	}
    	
    	/*System.out.println();
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
    	System.out.print(BLUE + ld_objects_paths + RESET + "\n");*/
    	
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
    			obj = obj.strip();
    			
    			boolean existed = false;
    			for(int x = 0; x < c_objects_names.size(); x++) {
    				if(existed) {
    					break;
    				}
    				if(c_objects_names.get(x).equals(obj)) {
    					existed = true;
    				}
    			}
    			for(int y = 0; y < asm_objects_names.size(); y++) {
    				if(existed) {
    					break;
    				}
    				if(asm_objects_names.get(y).equals(obj)) {
    					existed = true;
    				}
    			}
    			for(int u = 0; u < rust_objects_names.size(); u++) {
    				if(existed) {
    					break;
    				}
    				if(rust_objects_names.get(u).equals(obj)) {
    					existed = true;
    				}
    			}
    			for(int d = 0; d < ld_objects_names.size(); d++) {
    				if(existed) {
    					break;
    				}
    				if(ld_objects_names.get(d).equals(obj)) {
    					existed = true;
    				}
    			}
    			
    			if(!existed) {
    				String error = obj.replace("\n", "");
    				System.out.print(RED + "\nsForge Error : Undefined object -- > " + BLUE + error + RESET);
    				System.exit(0);
    			}
    			
    		} catch (Exception e) {
    			String error = g.replace("\n", "");
    			System.out.print(RED + "\nsForge Error : Error retrieving object name -- > " + BLUE + error + RESET);
    			System.exit(0);
    		}
    		
    		try {
    			func = g.substring(g.indexOf(".") + 1, g.lastIndexOf(")") + 1);
    		} catch(Exception e) {
    			String error = g.replace("\n", "");
    			System.out.print(RED + "\nsForge Error : Error retrieving function -- > " + BLUE + error + RESET);
    			System.exit(0);
    		}
    		
    		int f = 0;

    		for(int p = 0; p < c_objects_names.size(); p++) {
    			obj = obj.strip();
    			if(obj.equals(c_objects_names.get(p))) {
    				if(func.startsWith("compile")) {
    					c_command.clear();
    					
    					String out = func.substring(func.indexOf("(") + 1, func.lastIndexOf(","));
    					String std_ = func.substring(func.indexOf(",") + 1, func.lastIndexOf(")"));
    					std_ = std_.strip();
    					
    					c_command.add("gcc");
    					c_command.add(archtype);
    					
    					if(osdev) {
    						c_command.add("-ffreestanding");
    						c_command.add("-fno-pie");
    					}
    					if(std_.equals("no_std")) {
    						c_command.add("-nostdlib");
    					}
    					c_command.add("-c");
    					c_command.add(c_objects_paths.get(p));
    					c_command.add("-o");
    					c_command.add(path + c_objects_names.get(p) + "." + out);
    					
    					c_objects_names_o.add(c_objects_names.get(p) + "." + out);
    					
    					
    					ProcessBuilder c_sub = new ProcessBuilder(c_command);
    					c_sub.inheritIO();
    					try {
    						
    						Process c_proc = c_sub.start(); 	
    						
    						try {
    							int exitcode = c_proc.waitFor();
    							if(exitcode != 0) {
    								System.out.println("sForge GCC Error : " + exitcode);
    							}
    						} catch (Exception e) {
    							System.out.println("sForge Java Error : .waitFor --> " + BLUE + e);
    						}
    						
    						try {
    							TimeUnit.MILLISECONDS.sleep(delay);
    						} catch (Exception e) {
    							System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    						}
    						
    						if(print)System.out.println(GREEN + "sForge Sucess :" + YELLOW + " GCC (C)" + GREEN + " --> " + BLUE + c_objects_names.get(p) + RESET);
    						
    					} catch(IOException ioe) {
    						ioe.printStackTrace();
    						
    					}
    					
    					//System.out.println(c_command);
    					
    				} else {
    					System.out.print(RED + "\nsForge Error : Unknown function --> " + RESET);
    					System.out.print(BLUE + func + RESET);
    					System.exit(0);
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
    	    			nasm_command.clear();
    					
    					String out = func.substring(func.indexOf("(") + 1, func.lastIndexOf(",")).strip();
    					String type = func.substring(func.indexOf(",") + 1, func.lastIndexOf(")")).strip();
    			
    					nasm_command.add("nasm");
    					nasm_command.add("-f");
    					nasm_command.add(arch_type_asm);
    					nasm_command.add(asm_objects_paths.get(y));
    					
    					nasm_command.add("-o");
    					nasm_command.add(path + asm_objects_names.get(y) + "." + out);
    					
    					asm_objects_names_o.add(asm_objects_names.get(y) + "." + out);
    					
    					//System.out.println(nasm_command);

    				} else {
    					System.out.print(RED + "sForge Error : function -" + RESET);
    					System.out.print(YELLOW + func + RESET);
    					System.out.print(RED + "- is unknow ! \n");
    				}
    				
    				ProcessBuilder nasm_sub = new ProcessBuilder(nasm_command);
    				nasm_sub.inheritIO();
    				
    				try {
    					
    					Process nasm_proc = nasm_sub.start();
    					
						try {
							int exitcode = nasm_proc.waitFor();
							if(exitcode != 0) {
								System.out.println("sForge Nasm Error : " + exitcode);
								System.exit(0);
							}
						} catch (Exception e) {
							System.out.println("sForge Java Error : .waitFor --> " + BLUE + e);
						}
    					
    					try {
    						TimeUnit.MILLISECONDS.sleep(delay);
    					} catch (Exception e) {
    						System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    					}
    					
    					if(print)System.out.println(GREEN + "sForge Sucess :" + YELLOW + " NASM (Assembly)" + GREEN + " --> " + BLUE + asm_objects_names.get(y) + RESET);
    					
    				} catch (IOException ioe){
    					ioe.printStackTrace();
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
    					rs_command.clear();
    					
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
    					
    					rs_command.add("rustc");
    					rs_command.add("--emit=obj");
    					rs_command.add("-C");
    					rs_command.add("panic=abort");
    					rs_command.add("-C");
    					rs_command.add("opt-level=3");
    					rs_command.add("-C");
    					rs_command.add("overflow-checks=off");
    					rs_command.add("--target");
    					rs_command.add("i686-unknown-linux-gnu");
    					rs_command.add(rust_objects_paths.get(u));
    					rs_command.add("-o");
    					rs_command.add(path + rust_objects_names.get(u) + "." + out);
    					
    					rust_objects_names_o.add(rust_objects_names.get(u) + "." + out);
   
    					
    					ProcessBuilder rs_sub = new ProcessBuilder(rs_command);
    					rs_sub.inheritIO();
    					
    					try {
    						Process rs_proc = rs_sub.start();
    						
    						try {
    							int exitcode = rs_proc.waitFor();
    							if(exitcode != 0) {
    								System.out.println("sForge Cargo Error : " + exitcode);
    								System.exit(0);
    							}
    						} catch (Exception e) {
    							System.out.println("sForge Java Error : .waitFor --> " + BLUE + e);
    						}
    						
    						try {
    							TimeUnit.MILLISECONDS.sleep(delay);
    						} catch(Exception e) {
    							System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    						}
    						
    						if(print)System.out.println(GREEN + "sForge Sucess :" + YELLOW + " CARGO (Rust)" + GREEN + " --> " + BLUE + rust_objects_names.get(u) + RESET);
    						
    					} catch (IOException ioe) {
    						ioe.printStackTrace();
    					}
    					
    				} else {
    					System.out.print(RED + "sForge Error : function -" + RESET);
    					System.out.print(YELLOW + func + RESET);
    					System.out.print(RED + "- is unknow ! \n");
    				}
    				
    				f += 1;
    				obj_verif.add("true");
    			}
    			
    		}		

    		f = 0;
    		
    	}
    	
    	comp_analised = true;
    }
    
    static void link_analyse() {
    	String instr = link.substring(link.indexOf("{") + 1, link.lastIndexOf("}") - 1);
    	instr = instr.strip();
    	String[] all_instrs = instr.split(";");
    	
    	for(String joan : all_instrs) {
    		joan = joan.strip();
    		
    		if(joan.startsWith("ld")) {
    			ld_command.clear();
    			
    			String args = joan.substring(joan.indexOf("(") + 1, joan.lastIndexOf(")"));	
    			
    			File source = new File(ld_objects_paths.get(0));
    			File destination = new File(path + "/linker.ld");
    			String link_path = path + "linker.ld";
    			
    			try {
    				Path sourcePath = Paths.get(source.getAbsolutePath());
        			OutputStream destinationStream = new FileOutputStream(destination);
        			Files.copy(sourcePath, destinationStream);
    			} catch (IOException e) {
    				System.out.println("Erooooor");
    			}
    			
    			ld_command.add("ld");
    			ld_command.add("-m");
    			ld_command.add(arch_type_ld);
    			ld_command.add("-T");
    			ld_command.add(link_path);
    			
    			for(int x = 0; x < asm_objects_names_o.size(); x++) {
    				ld_command.add(path + asm_objects_names_o.get(x).strip());
    			}
    			for(int x = 0; x < c_objects_names_o.size(); x++) {
    				ld_command.add(path + c_objects_names_o.get(x).strip());
    			}
    			for(int x = 0; x < rust_objects_names_o.size(); x++) {
    				ld_command.add(path + rust_objects_names_o.get(x).strip());
    			}
    			
    			ld_command.add("-o");
    			ld_command.add(path + out_name + ".elf");
    
    			
    			ProcessBuilder ld_sub = new ProcessBuilder(ld_command);
    			ld_sub.directory(new File(path));
    			
    			if(infos) {
    				ld_sub.inheritIO();
    			}
    			
    			try {
    				Process ld_proc = ld_sub.start();
    				
					try {
						int exitcode = ld_proc.waitFor();
						if(exitcode != 0) {
							System.out.println("sForge Ld Error : " + exitcode);
							
							System.exit(0);
						}
					} catch (Exception e) {
						System.out.println("sForge Java Error : .waitFor --> " + BLUE + e);
					}
    				
    				try {
    					TimeUnit.MILLISECONDS.sleep(delay);
    				} catch (Exception e) {
    					System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    				}
    				
    				if(print)System.out.println(GREEN + "sForge Sucess :" + YELLOW + " LD (link)" + GREEN + " --> " + BLUE + ld_objects_names.get(0) + RESET);
    				
    			} catch (IOException ioe){
    				StringWriter sw = new StringWriter();
    				PrintWriter pw = new PrintWriter(sw);
    				ioe.printStackTrace(pw);
    				
    				String out = pw.toString();
    				System.out.println(out);
    			}
    			
    			
    		}else if(joan.startsWith("trans")) {
    			String out_type = joan.substring(joan.indexOf("(") + 1, joan.lastIndexOf(")"));
    			out_type = out_type.strip();
    			String m = "";
    			
    			if(out_type.equals("bin")) {
    				m = "binary";
    			}
    			
    			obj_command.add("objcopy");
    			obj_command.add("-O");
    			obj_command.add(m);
    			obj_command.add(path + out_name + ".elf");
    			obj_command.add(path + out_name + "." + out_type);
    			
    			main_out = out_name + "." + out_type;
    			
    			ProcessBuilder ob_sub = new ProcessBuilder(obj_command);
    			ob_sub.inheritIO();
    			
    			try {
    				Process ob_proc = ob_sub.start();
					
    				try {
						int exitcode = ob_proc.waitFor();
						if(exitcode != 0) {
							System.out.println("sForge Objcopy Error : " + exitcode);
							System.exit(0);
						}
					} catch (Exception e) {
						System.out.println("sForge Java Error : .waitFor --> " + BLUE + e);
					}
    				
    				try {
    					TimeUnit.MILLISECONDS.sleep(delay);
    				} catch (Exception e) {
    					System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    				}
    				
    				if(print)System.out.println(GREEN + "sForge Sucess :" + YELLOW + " OBJCOPY" + GREEN + " --> " + BLUE + out_type + RESET);
    				
    			} catch (IOException ioe) {
    				ioe.printStackTrace();
    			}
    			
    		} else {
    			System.out.println(RED + "sForge Error : unknow function here ->" + YELLOW + joan.strip() + RED + "<-" + RESET);
    		}
    	}
    	
    	link_analised = true;	
    }
    
    static void run_analyse() {
    	String instr = run.substring(run.indexOf("{") + 1, run.lastIndexOf("}") - 1);
    	instr = instr.strip();
    	String[] all_instrs = instr.split(";");
    	
    	for(String r : all_instrs) {
    		r = r.strip();
    		if(r.startsWith("qemu")) {
    			qemu_command.add("qemu-system-" + arch);
    			qemu_command.add("-drive");
    			qemu_command.add("format=raw,file=" + path + main_out);
    			
    			ProcessBuilder q_sub = new ProcessBuilder(qemu_command);
    			q_sub.inheritIO();
    			
    			try {
    				Process q_proc = q_sub.start();
    				
					try {
						int exitcode = q_proc.waitFor();
						if(exitcode != 0) {
							System.out.println("sForge QEMU error : " + exitcode);
							System.exit(0);
						}
					} catch (Exception e) {
						System.out.println("sForge Java Error : .waitFor --> " + BLUE + e);
					}
    				
    				try {
    					TimeUnit.MILLISECONDS.sleep(delay);
    				} catch(Exception e) {
    					System.out.println(RED + "Java Error TimeUnit" + e + RESET);
    				}
    				
    				if(print)System.out.println(GREEN + "sForge Sucess : " + YELLOW + "QEMU" + RESET);
    				
    			} catch (IOException ioe) {
    				ioe.printStackTrace();
    			}
    		}
    	}
    	
    	run_analised = true;
    }
    
    static void env_setup() {
    	String totalpath = project_path + "/" + project_name + "-" + branchname + "-sForge/env";
    	totalpath = totalpath.strip();
    	
    	File sf_loc_env = new File(totalpath + "/lingot");
    	
    	path = totalpath + "/lingot/";
    	path = path.strip();
    	
    	if(sf_loc_env.exists()) {
    		
    	} else {
    		sf_loc_env.mkdirs();
    	}
    	
    	env_set = true;
    }
}
