/**
@RestController
@RequestMapping("/combat")
public class CombatController {

    private final CombatService combatService;

    @Autowired
    public CombatController(CombatService combatService) {
        this.combatService = combatService;
    }

    @PostMapping("/attack")
    public String initiateAttack(@RequestBody AttackRequest request) {
        List<VillageTroops> attackingTroops = request.getAttackingTroops();
        List<VillageTroops> defendingTroops = request.getDefendingTroops();
        Village defendingVillage = request.getDefendingVillage();

        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);
        return "Attack completed";
    }
}
*/