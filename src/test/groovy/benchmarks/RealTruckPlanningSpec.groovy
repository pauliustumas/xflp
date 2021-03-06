package benchmarks

import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType
import xf.xflp.opt.construction.strategy.Strategy

import java.util.stream.IntStream

class RealTruckPlanningSpec extends Specification {

    def service = new XFLP()

    def "Mega trailer"() {
        service.addContainer().setWidth(248).setLength(1360).setHeight(300).setMaxWeight(25000)
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_PACKER)
        service.getParameter().setPreferredPackingStrategy(Strategy.WIDTH_PROPORTION)

        createItem(1, 120, 100, 100, 60,'G')
        createItem(2, 80, 100, 50, 70,'K')

        when:
        service.executeLoadPlanning()
        def result = service.getReport()

        then:
        result != null
        // This is theoretical max
        result.getSummary().getUtilization() > 0.92
    }

    private createItem(int bi, int l, int w, int h , int n, String stackGroup) {
        IntStream.range(0, n).forEach({ i ->
            service
                    .addItem()
                    .setExternID(bi+"-" + i)
                    .setWidth(w)
                    .setHeight(h)
                    .setLength(l)
                    .setWeight(1)
                    .setStackingWeightLimit(1000)
                    .setStackingGroup(stackGroup)
                    .setAllowedStackingGroups(stackGroup)
        })
    }

}
