using ClassLibrary.BusinessEntities;
using ClassLibrary.BusinessServices;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace API.Controllers
{
    public class CheckpointController : ApiController
    {
        ICheckpointServices checkpointServices = new CheckpointServices();

        /// <summary>
        /// Checkpoint.add
        /// </summary>
        /// <param name="checkpoint"></param>
        /// <returns></returns>
        [HttpPost]
        public HttpResponseMessage Add(Checkpoint checkpoint)
        {
            try
            {
                checkpointServices.Add(checkpoint);

                return Request.CreateResponse(HttpStatusCode.OK);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// Checkpoint.update
        /// </summary>
        /// <param name="checkpoint"></param>
        /// <returns></returns>
        [HttpPut]
        public HttpResponseMessage Update(Checkpoint checkpoint)
        {
            try
            {
                checkpointServices.Update(checkpoint);

                return Request.CreateResponse(HttpStatusCode.OK);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// Checkpoint.delete
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("api/checkpoint/delete/{id}")]
        public HttpResponseMessage Delete(int id)
        {
            try
            {
                checkpointServices.Delete(id);

                return Request.CreateResponse(HttpStatusCode.OK);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// Checkpoint.get
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("api/checkpoint/get/{id}")]
        public HttpResponseMessage Get(int id)
        {
            try
            {
                Checkpoint checkpoint = checkpointServices.Get(id);

                return Request.CreateResponse(HttpStatusCode.OK, checkpoint);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        [HttpGet]
        public HttpResponseMessage GetList()
        {
            try
            {
                List<Checkpoint> checkpointList = checkpointServices.GetList();

                return Request.CreateResponse(HttpStatusCode.OK, checkpointList);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }
    }
}