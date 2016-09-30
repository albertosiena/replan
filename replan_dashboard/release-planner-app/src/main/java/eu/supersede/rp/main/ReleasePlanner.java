/**
 * 
 */
package eu.supersede.rp.main;



import java.util.SortedSet;

import eu.supersede.rp.dto.Feature;
import eu.supersede.rp.dto.NewReleaseData;
import eu.supersede.rp.dto.NewResourceData;
import eu.supersede.rp.dto.NewSkillData;
import eu.supersede.rp.dto.Plan;
import eu.supersede.rp.dto.Project;
import eu.supersede.rp.dto.Release;
import eu.supersede.rp.dto.Resource;
import eu.supersede.rp.dto.Skill;
import eu.supersede.rp.dto.Status;

/**
 * @author farre
 *
 */
public interface ReleasePlanner {

	// Main
	// GET /projects/:project_id/features?status=pending
	public SortedSet<Feature> getPendingFeatures (long projec_id);
	
	// GET /projects/:project_id/releases
	public SortedSet<Release> getReleases (long project_id);

	// Re-plan a release (1)
	// GET /projects/:project_id/features/:feature_id
	public Feature getFeature (long project_id, long feature_id); // Probably not necessary
	
	// GET /projects/:project_id/skills
	public SortedSet<Skill> getProjectSkills (long project_id);
	
	// GET /projects/:project_id/releases/:release_id/features
	public SortedSet<Feature> getReleaseFeatures (long project_id, long release_id);
	
	// PUT /projects/:project_id/features/:feature_id
	public void modifyFeature (long project_id, Feature feature); // Feature includes id attribute
	
	// POST /projects/:project_id/releases/:release_id/features
	public Status addFeatureToRelease(long project_id, long feature_id, long release_id); // triggers re-planning
	
	// Re-plan a release (2)
	// DELETE /projects/:project_id/releases/:release_id/plan
	public void cancelLastReleasePlan (long project_id, long release_id);
	
	// Release details
	// GET /projects/:project_id/releases/:release_id/plan
	public Plan getReleasePlan (long project_id, long release_id);
	
	// DELETE /projects/:project_id/releases/:release_id/features/:feature_id
	public void removeFeatureFromRelease (long project_id, long feature_id, long release_id);
	
	//New release / release configuration
	// GET /projects/:project_id/resources
	public SortedSet<Resource> getProjectResources (long project_id);
	
	// PUT /projects/:project_id/releases/:release_id
	public Status modifyRelease (long project_id, Release release); // triggers re-planning
	
	// POST /projects/:project_id/releases
	public Release addNewReleaseToProject (long project_id, NewReleaseData nrd);
	
	// DELETE /projects/:project_id/releases/:release_id
    public Status deleteRelease (long project_id, long release_id); // triggers re-planning
	
	//Project configuration
	// GET /projects/:project_id
	public Project getProject (long project_id);
	
	// PUT /projects/:project_id
	public void modifyProject (long project_id, Project project);
	
	// PUT /projects/:project_id/resources/:resource_id
	public void modifyResource(long project_id, Resource resource);
	
	// POST /projects/:project_id/resources
	public Resource addNewResourceToProject (long project_id, NewResourceData nrd);
	
	// DELETE /projects/:project_id/resources/:resource_id
    public void deleteResource(long project_id, long resource_id);
	
	// POST /projects/:project_id/skills
	public Skill addNewSkillToProject (long project_id, NewSkillData nsd);
	
	// PUT //projects/:project_id/skills/:skill_id
	public void modifySkill (long project_id, Skill skill);
	
	// DELETE //projects/:project_id/skills/:skill_id
	public void deleteSkill (long project_id, long skill_id);
	
	
	
}
