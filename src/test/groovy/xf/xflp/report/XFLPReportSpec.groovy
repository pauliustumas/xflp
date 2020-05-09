package xf.xflp.report

import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType

class XFLPReportSpec extends Specification {

    def service = new XFLP()

    def "Test container id and item id allocation in result report with one container type"() {
        service.addContainer().setContainerType("CON1").setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_PACKER)

        when:
        service.executeLoadPlanning()
        def rep = service.getReport()
        then:
        rep.getContainerReports().size() == 1
        rep.getContainerReports().findAll {conRep -> conRep.getContainerTypeName() == "CON1"}.size() == 1
        rep.getContainerReports().get(0).getPackageEvents().find {pe -> pe.getId() == "P1"} != null
        rep.getContainerReports().get(0).getPackageEvents().find {pe -> pe.getId() == "P2"} != null
        rep.getContainerReports().get(0).getPackageEvents().find {pe -> pe.getId() == "P3"} != null
    }

    def "Test container id and item id allocation in result report with two container types"() {
        service.addContainer().setContainerType("CON1").setWidth(2).setLength(2).setHeight(2).setMaxWeight(1000)
        service.addContainer().setContainerType("CON2").setWidth(2).setLength(2).setHeight(2).setMaxWeight(1000)
        service.addItem().setExternID("P1").setWidth(2).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(2).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(2).setLength(1).setHeight(2).setWeight(1)
        service.addItem().setExternID("P4").setWidth(2).setLength(1).setHeight(2).setWeight(1)
        service.setTypeOfOptimization(XFLPOptType.DOUBLE_CONTAINER_ADD_PACKER)

        when:
        service.executeLoadPlanning()
        def rep = service.getReport()
        then:
        rep.getContainerReports().size() == 2
        rep.getContainerReports().findAll {conRep -> conRep.getContainerTypeName() == "CON1"}.size() == 1
        rep.getContainerReports().findAll {conRep -> conRep.getContainerTypeName() == "CON2"}.size() == 1
        rep.getContainerReports().get(0).getPackageEvents().find {pe -> pe.getId() == "P1"} != null
        rep.getContainerReports().get(0).getPackageEvents().find {pe -> pe.getId() == "P2"} != null
        rep.getContainerReports().get(1).getPackageEvents().find {pe -> pe.getId() == "P3"} != null
        rep.getContainerReports().get(1).getPackageEvents().find {pe -> pe.getId() == "P4"} != null
    }
}
