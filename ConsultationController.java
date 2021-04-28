@RequestMapping("/api/consultations")
public class ConsultationController {

    Logger log = LoggerFactory.getLogger(ConsultationController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;


    @Autowired
    TestRequestFlowService  testRequestFlowService;

    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/in-queue")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForConsultations()  {

        
        return testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED); 
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {

       
        User loggedInDoctor = userLoggedInService.getLoggedInUser(); 
        return testRequestQueryService.findByDoctor(loggedInDoctor); 
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {

       
        try {
            User loggedInDoctor = userLoggedInService.getLoggedInUser(); 
            return testRequestUpdateService.assignForConsultation(id,loggedInDoctor); 
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {

        

        try {
            User loggedInDoctor = userLoggedInService.getLoggedInUser(); 
            return testRequestUpdateService.updateConsultation(id,testResult,loggedInDoctor);
        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



}