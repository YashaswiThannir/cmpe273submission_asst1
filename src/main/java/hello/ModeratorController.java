
package hello;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableWebMvcSecurity
@RequestMapping(value="/api/v1")
@RestController
public class ModeratorController extends WebSecurityConfigurerAdapter {
	
	
	Moderator m = new Moderator();
	Polls p = new Polls();
	
	ArrayList <Moderator> list1 = new ArrayList<Moderator>();
	ArrayList <Polls> list2 = new ArrayList<Polls>();

	private static final AtomicLong counter = new AtomicLong(123455);
	
     int [] temp = new int[2];
     int [] result = new int[2];
     int [] newresult = new int[2];
	 String [] choice = new String[2];
    
	 protected void configure(HttpSecurity http) throws Exception {
                http
                .httpBasic().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/v1/").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/moderators/*").permitAll()
                .antMatchers("/api/v1/polls/*").permitAll()
                .antMatchers("/api/v1/moderators/*").fullyAuthenticated().anyRequest().hasRole("USER");
            }
         
         @Autowired
            public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth
                    .inMemoryAuthentication()
                        .withUser("foo").password("bar").roles("USER");
            }
    
    
    
    
	@RequestMapping(value = "/moderators/", method = RequestMethod.POST)
	
	public ResponseEntity <Moderator> moderator(@Valid @RequestBody Moderator m) {
		
		String date = new Date().toString();	
		m.setCreated_at(date);
		m.setId((int)counter.incrementAndGet());
		list1.add(m);
		
		return new ResponseEntity<Moderator>(m,HttpStatus.CREATED);
		
	   }

	@RequestMapping(value = "/moderators/{id}", method = RequestMethod.GET)
		public ResponseEntity <Moderator> Viewmoderator(@PathVariable int id) {
	
		int x = 0;
		
		System.out.println("size is"+list1.size());
		
		for(int i=0;i<list1.size();i++)
		{
			if(id == list1.get(i).getId())
			{
				x=i;
			}
		}
		
		return new ResponseEntity<Moderator>(list1.get(x),HttpStatus.OK);
	   
	}
	
	@RequestMapping(value = "/moderators/{id}", method = RequestMethod.PUT)
	 public ResponseEntity <Moderator> updatemoderator(@Valid @RequestBody Moderator m,@PathVariable int id) {
		
         int x = 0;
		
         String email = m.getEmail();
		 String password= m.getPassword();
		
		System.out.println("size is"+list1.size());
		for(int i=0;i<list1.size();i++)
		{
			if(id == list1.get(i).getId())
			{
			
				x=i;
				list1.get(i).setEmail(email);
				list1.get(i).setPassword(password);
				
			}
			
		}
		return new ResponseEntity<Moderator>(list1.get(x),HttpStatus.CREATED);
	   }
	
    @RequestMapping(value = "/moderators/{id}/polls", method = RequestMethod.POST)

	public ResponseEntity <Polls> createPoll(@Valid @RequestBody Polls p,@PathVariable int id) {
		
    	p.setId(Integer.toString((int) counter.incrementAndGet(),36));
    	list2.add(p);
    
    	
		for(int i=0;i<list1.size();i++)
		{
			if(id == list1.get(i).getId())
			{
				list1.get(i).getPollslist().add(p);
		
			}
			
		}
	
		return new ResponseEntity<Polls>(p,HttpStatus.CREATED);
		
	   }

	@RequestMapping(value = "/polls/{id1}", method = RequestMethod.GET)
		
	    public ResponseEntity <Polls> viewPollsWithoughResult(@PathVariable String id1) {
	
		int x = 0;
		
		System.out.println("size is"+list2.size());
		
		for(int i=0; i<list2.size(); i++)
		{
			if(id1.equals(list2.get(i).getId()))
			{
				x=i;
			}
		}
		
		return new ResponseEntity<Polls>(list2.get(x),HttpStatus.OK);
	}

	@RequestMapping(value = "/moderators/{id}/polls/{id1}", method = RequestMethod.GET)
	public ResponseEntity viewPollWithResult(@PathVariable int id,@PathVariable String id1) {

    int x = 0;
	System.out.println("size is"+list1.size());
	
	for(int i=0;i<list1.size();i++)
	{
		if(id == list1.get(i).getId())
		{
		
			for(int j=0;j<list2.size();j++)
			{
				if(id1.equals(list2.get(j).getId()))	
				{
					return new ResponseEntity(list1.get(i).getPollslist().get(j),HttpStatus.OK);
				}
			}
		}	
	
	}
	
     return new ResponseEntity("View Polls is not sucessfull",HttpStatus.OK);
}

	@RequestMapping(value = "/moderators/{id}/polls", method = RequestMethod.GET)
	public ResponseEntity listAllPolls(@PathVariable int id) {

    int x = 0;
	System.out.println("size is"+list1.size());
	
	for(int i=0;i<list1.size();i++)
	{
		if(id == list1.get(i).getId())
		{
		
					return new ResponseEntity(list1.get(i).getPollslist(),HttpStatus.OK);
		}	
	}
     return new ResponseEntity("View Polls is not sucessfull",HttpStatus.OK);
}

	
	
	@RequestMapping(value = "/moderators/{id}/polls/{id1}", method = RequestMethod.DELETE)
	public ResponseEntity deletePoll(@PathVariable int id,@PathVariable String id1) {

    int x = 0;
	System.out.println("size is"+list1.size());
	
	for(int i=0;i<list1.size();i++)
	{
		if(id == list1.get(i).getId())
		{
		
			for(int j=0;j<list2.size();j++)
			{
				if(id1.equals(list2.get(j).getId()))	
				{
					list2.remove(j);
					return new ResponseEntity(list1.get(i).getPollslist(),HttpStatus.NO_CONTENT);
				}
			}
		}	
	
	}
	
     return new ResponseEntity("Delete Polls is not sucessfull",HttpStatus.OK);
}

	
	
	 @RequestMapping(value = "/polls/{id1}", method = RequestMethod.PUT)
	 public ResponseEntity voteAPoll(@PathVariable String id1,@RequestParam(value="choice")int choice_index) 
	 {
		 for(int i=0;i<list2.size();i++)
		    {
			  if(id1.equals(list2.get(i).getId()))
			  {
			   
				  	if(choice_index == 0)
				  		{
				  		
				  		temp=list2.get(i).getResult();
				  		temp[choice_index]=temp[choice_index]+1;	
				  		list2.get(i).setResult(temp);
				  	 	return new ResponseEntity(HttpStatus.NO_CONTENT);
				  		
				  		}
				  	else if(choice_index==1)
		            {
				  		 temp=list2.get(i).getResult();
				  		 temp[choice_index]=temp[choice_index]+1;
						 list2.get(i).setResult(temp);
						 return new ResponseEntity(HttpStatus.NO_CONTENT);
		            }
				  	
    		    }
            }
		    	 
           	return new ResponseEntity("Not able to vote",HttpStatus.NO_CONTENT);
		
	   }

     @ExceptionHandler(MethodArgumentNotValidException.class)
     @ResponseBody
     public ResponseEntity handleBadInput(MethodArgumentNotValidException e)
     {
         String errors="";
         for(FieldError obj: e.getBindingResult().getFieldErrors())
             {
                 errors+=obj.getDefaultMessage();
             }    
         return new ResponseEntity(errors,HttpStatus.BAD_REQUEST);
     }
}